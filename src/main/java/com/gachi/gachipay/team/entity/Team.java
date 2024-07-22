package com.gachi.gachipay.team.entity;

import com.gachi.gachipay.account.entity.Account;
import com.gachi.gachipay.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@Table(
        uniqueConstraints = { // 스케쥴러가 바라 볼 컬럼
                @UniqueConstraint(
                        columnNames = {"id", "feeDueDate"}
                )
        }
)
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class) // 감사(auditing) 기능 활성화
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teamName; // 그룹 이름
    private String description; // 그룹 소개

    private Long representativeId; // 대표 멤버 아이디

    @OneToOne
    private Account representativeAccount; // 대표 계좌 정보

    private Long maxMembers; // 최대 인원

    private Long monthlyFee; // 월 회비
    private LocalDate feeDueDate; // 매달 돈 내는 날짜

    @OneToMany(mappedBy = "team")
    private List<TeamMembership> teamMemberships = new ArrayList<>(); // 그룹의 멤버들

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 팀 멤버십에 멤버 정보 추가
    public TeamMembership addMember(Member member) {
        if (this.teamMemberships == null) {
            this.teamMemberships = new ArrayList<>();
        }
        TeamMembership teamMembership = new TeamMembership();
        teamMembership.setTeam(this);
        teamMembership.setMember(member);
        teamMembership.setJoinedAt(LocalDateTime.now());

        teamMemberships.add(teamMembership);
        member.getTeamMemberships().add(teamMembership);

        return teamMembership;
    }
}
