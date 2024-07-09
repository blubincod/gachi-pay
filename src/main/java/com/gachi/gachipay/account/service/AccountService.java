package com.gachi.gachipay.account.service;

import com.gachi.gachipay.account.entity.Account;
import com.gachi.gachipay.account.model.AccountDto;
import com.gachi.gachipay.account.repository.AccountRepository;
import com.gachi.gachipay.common.exception.AccountException;
import com.gachi.gachipay.common.exception.ErrorCode;
import com.gachi.gachipay.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AccountService {
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;

    /**
     * 계좌 등록
     *
     * @param userId
     * @param accountDto
     * @return
     */
    public Account registerAccount(Long userId, AccountDto accountDto) {
        // 존재하는 사용자인지 확인
        boolean existsUser = memberRepository.existsById(userId);
        if (!existsUser) {
            throw new AccountException(
                    ErrorCode.USER_NOT_FOUND,
                    ErrorCode.USER_NOT_FOUND.getDescription());
        }

        // 이미 존재하는 계좌인지 확인
        boolean existsAccount = accountRepository.existsByAccountNumber(accountDto.getAccountNumber());
        if (existsAccount) {
            throw new AccountException(
                    ErrorCode.ACCOUNT_EXISTS,
                    ErrorCode.ACCOUNT_EXISTS.getDescription());
        }

        Account result = accountRepository.save(accountDto.toEntity(userId));

        return result;
    }

    /**
     * 계좌 정보 조회
     */
    public void getAccount() {

        System.out.println("GET ACCOUNT");
    }

    public void deleteAccount(
            AccountDto accountDto
    ) {
//        accountRepository.findById(accountDto.getUserId)
    }
}
