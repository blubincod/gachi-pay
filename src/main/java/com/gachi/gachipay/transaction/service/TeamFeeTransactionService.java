package com.gachi.gachipay.transaction.service;

import com.gachi.gachipay.account.entity.Account;
import com.gachi.gachipay.account.repository.AccountRepository;
import com.gachi.gachipay.common.exception.AccountException;
import com.gachi.gachipay.common.exception.ErrorCode;
import com.gachi.gachipay.member.repository.MemberRepository;
import com.gachi.gachipay.team.entity.Team;
import com.gachi.gachipay.team.repository.TeamMembershipRepository;
import com.gachi.gachipay.team.repository.TeamRepository;
import com.gachi.gachipay.transaction.entity.Transaction;
import com.gachi.gachipay.transaction.entity.TransactionResultType;
import com.gachi.gachipay.transaction.entity.TransactionType;
import com.gachi.gachipay.transaction.repository.TransactionRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.gachi.gachipay.transaction.entity.TransactionResultType.FAILURE;
import static com.gachi.gachipay.transaction.entity.TransactionResultType.SUCCESS;
import static com.gachi.gachipay.transaction.entity.TransactionType.USE;

@Slf4j
@Builder
@RequiredArgsConstructor
@Service
public class TeamFeeTransactionService {
    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final TeamRepository teamRepository;
    private final TeamMembershipRepository teamMembershipRepository;

    /**
     * 대표자의 계좌로 송금
     */
    @Transactional
    public void transferMoneyToTeamAccount(Team team, Account memberAccount) {
        Long amount = team.getMonthlyFee();
        Account representativeAccount = team.getRepresentativeAccount();

        if (memberAccount.getBalance() < amount) {
            throw new AccountException(ErrorCode.LACK_BALANCE);
        }

        memberAccount.setBalance(memberAccount.getBalance() - amount);
        representativeAccount.setBalance(representativeAccount.getBalance() + amount);

        accountRepository.save(memberAccount);
        accountRepository.save(representativeAccount);

        // 성공한 거래 내역 저장
        saveTeamFeeTransaction(USE, SUCCESS, memberAccount, amount);

        // TODO 입금 출금으로 저장
        // saveTeamFeeTransaction(USE, SUCCESS, representativeAccount, amount);
    }

    // 실패 시 거래 내역 저장
    @Transactional
    public void saveFailedTeamFeeTransaction(Team team, Account memberAccount) {
        saveTeamFeeTransaction(USE, FAILURE, memberAccount, team.getMonthlyFee());
    }

    // 거래 내역 저장
    @Transactional
    public void saveTeamFeeTransaction(
            TransactionType transactionType, TransactionResultType transactionResultType,
            Account account, Long amount
    ) {
        transactionRepository.save(
                Transaction.builder()
                        .transactionId(UUID.randomUUID().toString().replace("-", ""))
                        .transactionType(transactionType)
                        .transactionResult(transactionResultType)
                        .account(account)
                        .amount(amount)
                        .balanceSnapshot(account.getBalance())
                        .transactedAt(LocalDateTime.now())
                        .build()
        );
    }

    // 계좌 정보 조회
    private Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(
                        ErrorCode.ACCOUNT_NOT_FOUND));
    }

    // 팀 정보 가져오기
    private Team getTeam(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new AccountException(ErrorCode.TEAM_NOT_FOUND));
    }
}
