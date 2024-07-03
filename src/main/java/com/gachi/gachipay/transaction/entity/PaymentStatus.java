package com.gachi.gachipay.transaction.entity;

public enum PaymentStatus {
    RESERVE, //결제 예약(결제 생성)
    SUCCESS, //결제 성공
    FAILURE //결제 실패
}
