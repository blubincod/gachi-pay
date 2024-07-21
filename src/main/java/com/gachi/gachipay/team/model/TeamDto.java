package com.gachi.gachipay.team.model;

import com.gachi.gachipay.team.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamDto {
    private String teamName; // 그룹 이름

    private String description; // 그룹 설명

    private Long representativeId; // 대표 회원 아이디

    private Long maxMembers; // 최대 인원

    private Long monthlyFee; // 월 회비
    private LocalDate feeDueDate; // 매달 돈 내는 날짜

    public static TeamDto fromEntity(Team team){
        return TeamDto.builder()
                .teamName(team.getTeamName())
                .description(team.getDescription())
                .representativeId(team.getRepresentativeId())
                .maxMembers(team.getMaxMembers())
                .monthlyFee(team.getMonthlyFee())
                .feeDueDate(team.getFeeDueDate())
                .build();
    }
}
