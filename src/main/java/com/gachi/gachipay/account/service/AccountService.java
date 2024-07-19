package com.gachi.gachipay.account.service;

import com.gachi.gachipay.account.entity.Account;
import com.gachi.gachipay.account.model.AccountDto;
import com.gachi.gachipay.account.repository.AccountRepository;
import com.gachi.gachipay.common.exception.AccountException;
import com.gachi.gachipay.common.exception.ErrorCode;
import com.gachi.gachipay.member.entity.Member;
import com.gachi.gachipay.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.gachi.gachipay.account.entity.AccountStatus.IN_USE;
import static com.gachi.gachipay.account.entity.AccountStatus.UNREGISTERED;

@Slf4j
@Service
@AllArgsConstructor
public class AccountService {
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;

    /**
     * 계좌 등록
     */
    public AccountDto registerAccount(Long userId, AccountDto accountDto) {
        //  존재하는 사용자인지 확인
        Member member = getMember(userId);

        //  이미 존재하는 계좌인지 확인
        boolean existsAccount = accountRepository.existsByAccountNumber(accountDto.getAccountNumber());
        if (existsAccount) {
            throw new AccountException(
                    ErrorCode.ACCOUNT_EXISTS);
        }

        Account account = accountRepository.save(
                Account.builder()
                        .member(member)
                        .accountNumber(accountDto.getAccountNumber())
                        .balance(accountDto.getBalance())
                        .status(IN_USE)
                        .registeredAt(LocalDateTime.now())
                        .build());

        return AccountDto.fromEntity(account);
    }

    /**
     * 계좌 정보 조회
     */
    public List<AccountDto> getAccountsByUserId(Long userId) {
        // 회원 존재 여부 및 정보 가져오기
        Member member = getMember(userId);

        List<Account> accounts = accountRepository.findByMember(member);

        return accounts.stream()
                .map(AccountDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 계좌 해지
     */
    public void deleteAccount(Long userId, String accountNumber) {
        Member member = getMember(userId);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(
                        ErrorCode.USER_NOT_FOUND));


        validateDeleteAccount(member, account); // 계좌 해지를 위한 유효성 검사

        account.setStatus(UNREGISTERED);
        account.setUnregisteredAt(LocalDateTime.now());

        accountRepository.save(account);
    }

    /**
     * 계좌 해지를 위한 유효성 검사
     */
    private void validateDeleteAccount(Member member, Account account) {
        // 계좌 소유주 일치 확인
        if (member.getId() != account.getMember().getId()) {
            throw new AccountException(
                    ErrorCode.USER_ACCOUNT_UN_MATCH);
        }

        // 계좌의 잔액 확인
        if (account.getBalance() > 0) {
            throw new AccountException(
                    ErrorCode.BALANCE_NOT_EMPTY);
        }
    }

    // 회원 정보 가져오기
    private Member getMember(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new AccountException(ErrorCode.USER_NOT_FOUND));
        return member;
    }
}
