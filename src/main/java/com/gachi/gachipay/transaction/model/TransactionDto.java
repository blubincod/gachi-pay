package com.gachi.gachipay.transaction.model;

import com.gachi.gachipay.transaction.entity.Transaction;
import com.gachi.gachipay.transaction.entity.TransactionResultType;
import com.gachi.gachipay.transaction.entity.TransactionType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private String accountNumber; // 거래 계좌번호
    private String transactionId; // 거래 고유 아이디
    private String transactionTitle; // 거래 제목
    private TransactionType transactionType; // 거래 유형(사용/취소)
    private TransactionResultType transactionResult;
    private Long amount; // 거래 금액
    private Long paidAmount; // 결제된 금액
    private Long refundAmount; // 환불된 금액
    private Long balanceSnapshot;
    private LocalDateTime transactedAt; // 거래 일시
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TransactionDto fromEntity(Transaction transaction) {
        return TransactionDto.builder()
                .accountNumber(transaction.getAccount().getAccountNumber())
                .transactionId(transaction.getTransactionId())
                .transactionType(transaction.getTransactionType())
                .transactionResult(transaction.getTransactionResult())
                .amount(transaction.getAmount())
                .balanceSnapshot(transaction.getBalanceSnapshot())
                .transactedAt(transaction.getTransactedAt())
                .build();
    }
}
