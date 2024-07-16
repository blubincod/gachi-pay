package com.gachi.gachipay.transaction.service;

import com.gachi.gachipay.account.entity.Account;
import com.gachi.gachipay.account.entity.AccountStatus;
import com.gachi.gachipay.account.repository.AccountRepository;
import com.gachi.gachipay.common.exception.AccountException;
import com.gachi.gachipay.common.exception.ErrorCode;
import com.gachi.gachipay.member.entity.Member;
import com.gachi.gachipay.member.repository.MemberRepository;
import com.gachi.gachipay.transaction.entity.Transaction;
import com.gachi.gachipay.transaction.entity.TransactionResultType;
import com.gachi.gachipay.transaction.model.TransactionDto;
import com.gachi.gachipay.transaction.repository.TransactionRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.gachi.gachipay.transaction.entity.TransactionResultType.FAILURE;
import static com.gachi.gachipay.transaction.entity.TransactionResultType.SUCCESS;
import static com.gachi.gachipay.transaction.entity.TransactionType.USE;

@Slf4j
@Builder
@RequiredArgsConstructor
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;

    /**
     * 잔액 사용
     */
    public TransactionDto useBalance(Long userId, String accountNumber, Long amount) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new AccountException(
                        ErrorCode.USER_NOT_FOUND,
                        ErrorCode.USER_NOT_FOUND.getDescription()));
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(
                        ErrorCode.ACCOUNT_NOT_FOUND,
                        ErrorCode.ACCOUNT_NOT_FOUND.getDescription()));

        validateUseBalance(member, account, amount); //유효성 검사 메서드 호출

        account.useBalance(amount); //잔액 부족 검사 메서드 호출

        Transaction transaction = saveAndGetTransaction(SUCCESS, amount, account);

        return TransactionDto.fromEntity(transaction);
    }

    /**
     * 유효성 검사
     * 1. 계좌 소유자 일치 확인
     * 2. 계좌 상태 확인(사용중인 계좌 / 해지된 계좌)
     * 3. 잔액 부족 확인
     */
    private void validateUseBalance(Member member, Account account, Long amount) {
        if (member.getId() != account.getMember().getId()) {
            throw new AccountException(
                    ErrorCode.USER_ACCOUNT_UN_MATCH,
                    ErrorCode.USER_ACCOUNT_UN_MATCH.getDescription());
        }

        if (account.getStatus() != AccountStatus.IN_USE) {
            throw new AccountException(
                    ErrorCode.ACCOUNT_ALREADY_UNREGISTERED,
                    ErrorCode.ACCOUNT_ALREADY_UNREGISTERED.getDescription());
        }

        if (account.getBalance() < amount) {
            throw new AccountException(
                    ErrorCode.LACK_BALANCE,
                    ErrorCode.LACK_BALANCE.getDescription());
        }
    }

    /**
     * 거래 실패
     *
     * @param accountNumber
     * @param amount
     */
    public void saveFailedTransaction(String accountNumber, Long amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(
                        ErrorCode.ACCOUNT_NOT_FOUND,
                        ErrorCode.ACCOUNT_NOT_FOUND.getDescription()));

        saveAndGetTransaction(FAILURE, amount, account);
    }

    private Transaction saveAndGetTransaction(TransactionResultType transactionResultType, Long amount, Account account) {
        return transactionRepository.save(
                Transaction.builder()
                        .transactionId(UUID.randomUUID().toString().replace("-", ""))
                        .transactionType(USE)
                        .transactionResult(transactionResultType)
                        .account(account)
                        .amount(amount)
                        .balanceSnapshot(account.getBalance())
                        .transactedAt(LocalDateTime.now())
                        .build()
        );
    }
}
