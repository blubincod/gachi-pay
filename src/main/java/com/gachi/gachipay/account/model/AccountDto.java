package com.gachi.gachipay.account.model;

import com.gachi.gachipay.account.entity.Account;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
public class AccountDto {
    private Long userId; //계좌 소유자 아이디

    private String accountNumber; //계좌번호
    private Long balance; //잔고
    private int status; //계좌 상태

    private LocalDateTime registeredAt; //계좌 등록 일시
    private LocalDateTime unregisteredAt; //계좌 해지 일시

    private boolean isGroupAccount; //그룹 계좌 여부(그룹 계좌X(default) : false | 그룹 계좌O : true)

    public Account toEntity(Long userId){
        return Account.builder()
                .userId(userId)
                .accountNumber(this.accountNumber)
                .balance(this.balance)
                .registeredAt(LocalDateTime.now())
                .build();
    }
}