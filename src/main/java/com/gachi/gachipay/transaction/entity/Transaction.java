package com.gachi.gachipay.transaction.entity;

import com.gachi.gachipay.account.entity.Account;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 아이디

    private String transactionId; // 거래 고유 아이디
    private String transactionTitle; // 거래 제목

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType; // 거래 유형
    @Enumerated(EnumType.STRING)
    private TransactionResultType transactionResult;

    @ManyToOne
    private Account account;

    private Long amount; // 거래 금액
    private Long paidAmount; // 결제된 금액
    private Long refundAmount; // 환불된 금액
    private Long balanceSnapshot;

    private LocalDateTime transactedAt; // 거래 일시

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
