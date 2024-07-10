package com.gachi.gachipay.account.controller;

import com.gachi.gachipay.account.entity.Account;
import com.gachi.gachipay.account.model.AccountDto;
import com.gachi.gachipay.account.model.UnregisterAccount;
import com.gachi.gachipay.account.repository.AccountRepository;
import com.gachi.gachipay.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    /**
     * 계좌 정보 등록
     *
     * @param userId
     * @param accountDto
     * @return
     */
    @PostMapping("/account")
    public ResponseEntity<?> registerAccount(
            @RequestParam("user_id") Long userId,
            @RequestBody @Valid AccountDto accountDto
    ) {
        System.out.println("Registration is in progress...");
        Account result = accountService.registerAccount(userId, accountDto);

        return ResponseEntity.ok(result);
    }

    /**
     * 계좌 정보 조회
     *
     * @return
     */
    @GetMapping("/account")
    public ResponseEntity<?> getAccount(
            @RequestParam("user_id") Long userId
    ) {
        System.out.println("GET ACCOUNT:)" + userId);

        return ResponseEntity.ok(null);
    }

    /**
     * 계좌 정보 수정
     *
     * @return
     */
    @PutMapping("/account/{id}")
    public ResponseEntity<?> modifyAccount() {

        return ResponseEntity.ok(null);
    }

    /**
     * 계좌 해지
     *
     * @return
     */
    @DeleteMapping("/account")
    public ResponseEntity<?> deleteAccount(
            @RequestBody @Valid UnregisterAccount.Request request
    ) {
        accountService.deleteAccount(
                request.getUserId(),
                request.getAccountNumber());

        return ResponseEntity.ok("deleted");
    }
}
