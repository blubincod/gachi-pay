package com.gachi.gachipay.account.entity;

import com.gachi.gachipay.common.exception.AccountException;
import com.gachi.gachipay.common.exception.ErrorCode;
import com.gachi.gachipay.member.entity.Member;
import com.gachi.gachipay.team.entity.Team;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class) // 감사(auditing) 기능 활성화
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 아이디

    @ManyToOne
    private Member member; // 사용자

    @ManyToOne
    private Team team; // 그룹 (대표 계좌인 경우에만 설정)

    private boolean isRepresentativeAccount; // 대표 계좌 여부

    @NotBlank
    private String accountNumber; // 계좌번호

    @NotNull
    private Long balance; // 잔고

    @Enumerated(EnumType.STRING)
    private AccountStatus status; // 계좌 상태

    private LocalDateTime registeredAt; // 계좌 등록 일시
    private LocalDateTime unregisteredAt; // 계좌 해지 일시

    // 잔고에서 거래 사용 금액 차감
    public void useBalance(Long amount) {
        if (this.balance < amount) {
            throw new AccountException(
                    ErrorCode.LACK_BALANCE);
        }
        this.balance -= amount;
    }

    // 잔고에서 거래 취소 금액 증액
    public void cancelBalance(Long amount) {
        if (amount < 0) {
            throw new AccountException(
                    ErrorCode.INVALID_REQUEST);
        }

        this.balance += amount;
    }
}
