package com.gachi.gachipay.transaction.model;

import com.gachi.gachipay.transaction.entity.TransactionResultType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class CancelBalance {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {
        @NotNull
        private String transactionId;

        @NotBlank
        private String accountNumber;

        @NotNull
        private Long amount;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String accountNumber;
        private String transactionId;
        private TransactionResultType transactionResult;
        private Long amount;
        private Long paid_amount; //지불된 금액
        private Long refund_amount; //환불된 금액
        private LocalDateTime transactedAt; //거래 일시

        public static Response from(TransactionDto transactionDto) {
            return Response.builder()
                    .accountNumber(transactionDto.getAccountNumber())
                    .transactionId(transactionDto.getTransactionId())
                    .transactionResult(transactionDto.getTransactionResult())
                    .amount(transactionDto.getAmount())
                    .transactedAt(transactionDto.getTransactedAt())
                    .build();
        }
    }
}
