package com.gachi.gachipay.account.controller;

import com.gachi.gachipay.account.model.AccountDto;
import com.gachi.gachipay.account.model.UnregisterAccount;
import com.gachi.gachipay.account.repository.AccountRepository;
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
public class AccountController {
    private final AccountService accountService;
//    private final RedisTestService redisTestService;

//    @GetMapping("/get-lcok")
//    public String getLock() {
//        System.out.println("HELLO CONTROLLER");
//        return redisTestService.getLock();
//    }

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
        AccountDto result = accountService.registerAccount(userId, accountDto);

        return ResponseEntity.ok(result);
    }

    /**
     * 계좌 정보 조회
     *
     * @return
     */
    @GetMapping("/account")
    public ResponseEntity<?> getAccountsByUserId(
            @RequestParam("user_id") Long userId
    ) {
        List<AccountDto> accounts = accountService.getAccountsByUserId(userId);

        return ResponseEntity.ok(accounts);
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
