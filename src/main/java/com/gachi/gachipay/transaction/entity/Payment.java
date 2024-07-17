package com.gachi.gachipay.transaction.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id; // 아이디

    // 결제 고유 아이디
    String product; // 결제 상품
    String method; // 결제 금액
    Long balanceSnapshot; // 거래 후 계좌 잔액
    Long storeId; // 가맹점 아이디

    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus; // 결제 처리 상태

    LocalDateTime requestedAt; // 결제 일시
    LocalDateTime approvedAt; // 결제 승인 일시
}
