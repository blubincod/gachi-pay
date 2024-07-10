package com.gachi.gachipay.account.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class) //감사(auditing) 기능 활성화
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //아이디

    @NotNull
    private Long userId; //계좌 소유자 아이디

    @NotNull
    private String accountNumber; //계좌번호

    @NotNull
    private Long balance; //잔고

    @Enumerated(EnumType.STRING)
    private AccountStatus status; //계좌 상태

    private LocalDateTime registeredAt; //계좌 등록 일시
    private LocalDateTime unregisteredAt; //계좌 해지 일시

    private boolean isGroupAccount; //그룹 계좌 여부(그룹 계좌X(default) : false | 그룹 계좌O : true)

}
