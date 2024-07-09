package com.gachi.gachipay.account.controller;

import com.gachi.gachipay.account.model.AccountDto;
import com.gachi.gachipay.account.repository.AccountRepository;
import com.gachi.gachipay.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @GetMapping("/account/test")
    public ResponseEntity<?> testAccount() {
        accountService.testAccount();

        return ResponseEntity.ok(null);
    }

    /**
     * 계좌 등록
     * @param account
     * @return
     */
    @PostMapping("/account")
    public ResponseEntity<?> registerAccount(
            @RequestBody @Valid AccountDto account
    ) {
        System.out.println("REGISTERING...");

        var result = accountService.registerAccount(account);

        return ResponseEntity.ok(result);
    }

    /**
     * 계좌 정보 조회
     * @return
     */
    @GetMapping("/account/{id}")
    public ResponseEntity<?> getAccount() {
        System.out.println("GET ACCOUNT:)");

        return ResponseEntity.ok(null);
    }
}
