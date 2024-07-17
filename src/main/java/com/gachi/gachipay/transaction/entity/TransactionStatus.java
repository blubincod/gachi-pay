package com.gachi.gachipay.transaction.entity;

public enum TransactionStatus {
    CREATED, // 거래 생성
    FAILED, // 거래 실패
    PAID, // 지불
    CANCELED, // 거래 취소
    REFUNDED, // 환불
    PARTIAL_REFUNDED // 부분 환불
}
