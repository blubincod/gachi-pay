package com.gachi.gachipay.team.entity;

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
        uniqueConstraints = {
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

    private Long maxMembers; // 최대 인원

    private Long monthlyFee; // 월 회비
    private LocalDate feeDueDate; // 매달 돈 내는 날짜

    @OneToMany(mappedBy = "team")
    private List<TeamMembership> teamMemberships = new ArrayList<>(); // 그룹의 멤버들

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
