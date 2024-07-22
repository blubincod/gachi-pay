package com.gachi.gachipay.transaction.controller;

import com.gachi.gachipay.common.aop.AccountLock;
import com.gachi.gachipay.common.exception.AccountException;
import com.gachi.gachipay.transaction.model.*;
import com.gachi.gachipay.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 거래 관련 컨트롤러
 * 개인(Member)
 * 1. 개인 잔액 사용
 * 2. 개인 잔액 사용 취소
 * <p>
 * 그룹(Team)
 * 1. 그룹 잔액 사용
 * 2. 그룹 잔액 사용 취소
 * <p>
 * 거래 정보 조회
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    /**
     * 잔액 사용
     */
    @AccountLock
    @PostMapping("/use")
    public UseBalance.Response useBalance(
            @RequestBody @Valid UseBalance.Request request
    ) throws InterruptedException {
        try {
            return UseBalance.Response.fromDto(
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

    /**
     * 잔액 사용 취소
     */
    @AccountLock
    @PostMapping("/cancel")
    public CancelBalance.Response cancelBalance(
            @RequestBody @Valid CancelBalance.Request request
    ) {
        try {
            return CancelBalance.Response.fromDto(
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

    /**
     * 그룹 잔액 사용
     */
    @AccountLock
    @PostMapping("/team/{teamId}/use")
    public UseTeamBalance.Response useTeamBalance(
            @PathVariable Long teamId,
            @RequestBody @Valid UseTeamBalance.Request request
    ) throws InterruptedException {
        try {
            return UseTeamBalance.Response.fromDto(
                    transactionService.useTeamBalance(
                            teamId,
                            request.getMemberId(),
                            request.getAmount()));
        } catch (AccountException e) {
            log.error("Failed to use TeamBalance");

            transactionService.saveFailedUseTeamTransaction(
                    teamId,
                    request.getMemberId(),
                    request.getAmount()
            );

            throw e;
        }
    }

    /**
     * 그룹 잔액 사용 취소
     */
    @AccountLock
    @PostMapping("/team/{teamId}/cancel")
    public CancelTeamBalance.Response cancelTeamBalance(
            @PathVariable Long teamId,
            @RequestBody @Valid CancelTeamBalance.Request request
    ) {
        try {
            return CancelTeamBalance.Response.fromDto(
                    transactionService.cancelTeamBalance(
                            teamId,
                            request.getTransactionId(),
                            request.getAmount()));
        } catch (AccountException e) {
            log.error("Failed to cancel TeamBalance");

            transactionService.saveFailedCancelTeamTransaction(
                    teamId,
                    request.getTransactionId()
            );

            throw e;
        }
    }

    // 거래 정보 조회
    @GetMapping("/{transactionId}")
    public QueryTransactionResponse queryTransactionResponse(
            @PathVariable String transactionId
    ) {
        return QueryTransactionResponse.from(
                transactionService.queryTransaction(transactionId));
    }

    // TODO 개인 거래 목록 조회

    // TODO 팀 거래 목록 조회
}
