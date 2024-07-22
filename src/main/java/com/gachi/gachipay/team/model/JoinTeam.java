package com.gachi.gachipay.team.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

public class JoinTeam {

    @Data
    public static class Request {
        @NotNull
        private Long memberId;

        @NotBlank
        private String accountNumber;
    }
    @Data
    @Builder
    public static class Response {
        private Long teamId; // 그룹 아이디
        private String teamName; // 그룹 이름
        private String description; // 그룹 설명
        private Long representativeId; // 대표 회원 아이디
        private Long maxMembers; // 최대 인원
        private Long monthlyFee; // 월 회비
        private LocalDate feeDueDate; // 매달 돈 내는 날짜

        public static JoinTeam.Response fromDto(TeamDto teamDto) {
            return JoinTeam.Response.builder()
                    .teamId(teamDto.getTeamId())
                    .representativeId(teamDto.getRepresentativeId())
                    .teamName(teamDto.getTeamName())
                    .description(teamDto.getDescription())
                    .maxMembers(teamDto.getMaxMembers())
                    .monthlyFee(teamDto.getMonthlyFee())
                    .build();
        }
    }

}
