package com.gachi.gachipay.team.model;

import com.gachi.gachipay.team.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamDto {
    private String name; // 그룹 이름

    private String description; // 그룹 설명

    private Long representativeId; // 대표 회원 아이디

    public static TeamDto fromEntity(Team team){
        return TeamDto.builder()
                .name(team.getTeamName())
                .description(team.getDescription())
                .representativeId(team.getRepresentativeId())
                .build();
    }
}
