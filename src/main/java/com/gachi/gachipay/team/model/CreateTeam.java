package com.gachi.gachipay.team.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

public class CreateTeam {
    @Data
    public static class Request{
        @NotNull
        private Long memberId;

        @NotBlank
        private String accountNumber;

        @NotBlank
        private String teamName;

        @NotBlank
        private String description;

        @NotNull
        private Long maxMembers;

        @NotNull
        private Long monthlyFee;

        private LocalDate feeDueDate;
    }
}
