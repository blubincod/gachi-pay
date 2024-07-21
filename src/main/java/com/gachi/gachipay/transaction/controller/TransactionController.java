package com.gachi.gachipay.transaction.controller;

import com.gachi.gachipay.common.aop.AccountLock;
import com.gachi.gachipay.common.exception.AccountException;
import com.gachi.gachipay.transaction.model.CancelBalance;
import com.gachi.gachipay.transaction.model.QueryTransactionResponse;
import com.gachi.gachipay.transaction.model.UseBalance;
import com.gachi.gachipay.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 거래 관련 컨트롤러
 * 1. 잔액 사용
 * 2. 잔액 사용 취소
 * 3. 거래 확인
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    // 잔액 사용
    @AccountLock
    @PostMapping("/use")
    public UseBalance.Response useBalance(
            @RequestBody @Valid UseBalance.Request request
    ) throws InterruptedException {
        try {
            Thread.sleep(3000L);
            return UseBalance.Response.from(
                    transactionService.useBalance(
                            request.getMemberId(),
                            request.getAccountNumber(),
                            request.getAmount()));
        } catch (AccountException e) {
            log.error("Failed to use Balance");

            transactionService.saveFailedUseTransaction(
                    request.getAccountNumber(),
                    request.getAmount()
            );

            throw e;
        }
    }

    // 잔액 사용 취소
    @AccountLock
    @PostMapping("/cancel")
    public CancelBalance.Response cancelBalance(
            @RequestBody @Valid CancelBalance.Request request
    ) {
        try {
            return CancelBalance.Response.from(
                    transactionService.cancelBalance(
                            request.getTransactionId(),
                            request.getAccountNumber(),
                            request.getAmount()));
        } catch (AccountException e) {
            log.error("Failed to cancel Balance");

            transactionService.saveFailedCancelTransaction(
                    request.getAccountNumber(),
                    request.getAmount()
            );

            throw e;
        }
    }

    // 거래 관련 Query
    @GetMapping("/{transactionId}")
    public QueryTransactionResponse queryTransactionResponse(
            @PathVariable String transactionId
    ) {
        return QueryTransactionResponse.from(
                transactionService.queryTransaction(transactionId));
    }
}
