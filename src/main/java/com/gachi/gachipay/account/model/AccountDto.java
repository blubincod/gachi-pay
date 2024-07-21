package com.gachi.gachipay.account.model;

import com.gachi.gachipay.account.entity.Account;
import com.gachi.gachipay.account.entity.AccountStatus;
import com.gachi.gachipay.member.entity.Member;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import static com.gachi.gachipay.account.entity.AccountStatus.IN_USE;

@Data
@Builder
public class AccountDto {
    private Long memberId; //  계좌 소유자 아이디

    private String accountNumber; //  계좌번호
    private Long balance; //  잔고

    private LocalDateTime registeredAt; //  계좌 등록 일시
    private LocalDateTime unregisteredAt; //  계좌 해지 일시

    private AccountStatus status; //  계좌 상태

    /**
     * Account -> AccountDto
     */
    public static AccountDto fromEntity(Account account) {
        return AccountDto.builder()
                .memberId(account.getMember().getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .status(account.getStatus())
                .registeredAt(account.getRegisteredAt())
                .build();
    }

    /**
     * Member -> Account
     */
    public Account toEntity(Member member) {
        return Account.builder()
                .member(member)
                .accountNumber(this.accountNumber)
                .balance(this.balance)
                .status(IN_USE)
                .registeredAt(LocalDateTime.now())
                .build();
    }
}