package com.gachi.gachipay.account.service;

import com.gachi.gachipay.account.entity.Account;
import com.gachi.gachipay.account.model.AccountDto;
import com.gachi.gachipay.account.repository.AccountRepository;
import com.gachi.gachipay.common.exception.AccountException;
import com.gachi.gachipay.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public void testAccount() {
        System.out.println("TEST Success:)");
    }

    /**
     * 계좌 등록
     * @param accountDto
     * @return
     */
    public Account registerAccount(AccountDto accountDto) {
        boolean exists = accountRepository.existsByAccountNumber(accountDto.getAccountNumber());

        if (exists) {
            throw new AccountException(ErrorCode.ACCOUNT_EXISTS);
        }

        var result = accountRepository.save(accountDto.toEntity());

        return result;
    }

    /**
     * 계좌 정보 조회
     */
    public void getAccount() {
        System.out.println("GET ACCOUNT");
    }

}
