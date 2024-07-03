package com.gachi.gachipay.account.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id; //아이디

    String accountNumber; //계좌번호

    long balance; //잔고

    int status; //계좌 상태

    LocalDateTime registeredAt; //계좌 등록 일시
    LocalDateTime unregisteredAt; //계좌 해지 일시

    boolean isGroupAccount; //그룹 계좌 여부(그룹 계좌X(default) : false | 그룹 계좌O : true)

}
