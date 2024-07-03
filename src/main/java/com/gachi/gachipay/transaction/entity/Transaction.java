package com.gachi.gachipay.transaction.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id; //아이디

    //거래 고유 아이디
    String transactionTitle; //거래 이름

    @Enumerated(EnumType.STRING)
    TransactionStatus transactionStatus; //거래 상태

    Long transactionAmount; //거래 금액

    Long paidAmount; //결제된 금액

    Long refundAmount; //환불된 금액

    LocalDateTime transactedAt; //거래 일시

}
