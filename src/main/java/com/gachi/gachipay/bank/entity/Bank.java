package com.gachi.gachipay.bank.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id; //은행 아이디

    String bankName; //은행 이름

    String owner; //계좌 소유자

    long balance; //잔고

}
