package com.gachi.gachipay.account.controller;

import com.gachi.gachipay.account.model.AccountDto;
import com.gachi.gachipay.account.model.UnregisterAccount;
import com.gachi.gachipay.account.service.AccountService;
import com.gachi.gachipay.common.service.RedisTestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;
    private final RedisTestService redisTestService;

    /**
     * lock test
     */
    @GetMapping("/get-lock")
    public String getLock() {
        return redisTestService.getLock();
    }

    /**
     * 계좌 등록
     */
    @PostMapping
    public ResponseEntity<AccountDto> registerAccount(
            @RequestParam("member_id") Long memberId,
            @RequestBody @Valid AccountDto accountDto
    ) {
        AccountDto accountInfo = accountService.registerAccount(memberId, accountDto);

        return ResponseEntity.ok(accountInfo);
    }

    /**
     * 계좌 정보 조회
     */
    @GetMapping
    public ResponseEntity<List<AccountDto>> getAccounts(
            @RequestParam("member_id") Long memberId
    ) {
        List<AccountDto> accounts = accountService.getAccounts(memberId);

        return ResponseEntity.ok(accounts);
    }

    /**
     * 계좌 정보 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> modifyAccount() {

        return ResponseEntity.ok(null);
    }

    /**
     * 계좌 해지
     */
    @DeleteMapping
    public ResponseEntity<?> deleteAccount(
            @RequestBody @Valid UnregisterAccount.Request request
    ) {
        accountService.deleteAccount(
                request.getMemberId(),
                request.getAccountNumber());

        return ResponseEntity.noContent().build();
    }
}
