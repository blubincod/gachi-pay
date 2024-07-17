package com.gachi.gachipay.transaction.model;

import com.gachi.gachipay.transaction.entity.TransactionResultType;
import com.gachi.gachipay.transaction.entity.TransactionType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryTransactionResponse {

    private String accountNumber;
    private String transactionId;
    private TransactionType transactionType;
    private TransactionResultType transactionResult;
    private Long amount;
    private Long paid_amount; //지불된 금액
    private Long refund_amount; //환불된 금액
    private LocalDateTime transactedAt; //거래 일시

    public static QueryTransactionResponse from(TransactionDto transactionDto) {
        return QueryTransactionResponse.builder()
                .accountNumber(transactionDto.getAccountNumber())
                .transactionId(transactionDto.getTransactionId())
                .transactionType(transactionDto.getTransactionType())
                .transactionResult(transactionDto.getTransactionResult())
                .amount(transactionDto.getAmount())
                .transactedAt(transactionDto.getTransactedAt())
                .build();
    }
}
