package com.gachi.gachipay.team.entity;

import com.gachi.gachipay.member.entity.Member;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 회원과 그룹의 관계 엔티티
 * 회비 납부 여부
 * 납부된 회비
 * 총 납부 회비
 */
@Entity
@EntityListeners(AuditingEntityListener.class) // 감사(auditing) 기능 활성화
public class TeamMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    private String role; // 역할(대표 회원, 일반 회원)
    private String status; // 상태(활동 중, 탈퇴)

    @CreatedDate
    private LocalDate joinedAt; // 가입 일자
    private LocalDate leftAt; // 탈퇴 일자

    @LastModifiedDate
    private LocalDateTime updatedAt; // 수정 일시
}
