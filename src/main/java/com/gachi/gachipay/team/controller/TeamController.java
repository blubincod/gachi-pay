package com.gachi.gachipay.team.controller;

import com.gachi.gachipay.team.model.CreateTeam;
import com.gachi.gachipay.team.model.TeamDto;
import com.gachi.gachipay.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/team")
public class TeamController {
    private final TeamService teamService;

    /**
     * 그룹 생성
     */
    @PostMapping
    public ResponseEntity<?> createTeam(
            @RequestBody @Valid CreateTeam.Request request
    ) {
        teamService.createTeam(request);

        return ResponseEntity.ok("Created");
    }

    /**
     * 그룹 정보 조회
     */
    @GetMapping("/{teamId}")
    public ResponseEntity<?> getTeamInfo(
            @PathVariable Long teamId
    ) {
        TeamDto teamInfo = teamService.getTeamInfo(teamId);

        return ResponseEntity.ok(teamInfo);
    }

    /**
     * 그룹 삭제
     */
    @DeleteMapping("/{teamId}")
    public ResponseEntity<?> deleteTeam(
            @PathVariable Long teamId,
            @RequestParam("member_id") Long memberId
    ) {
        teamService.deleteTeam(teamId, memberId);

        return ResponseEntity.ok("Deleted");
    }

    /**
     * 그룹 가입
     */
    @PostMapping("/join")
    public void joinTeam(
            @RequestParam("member_id") Long memberId
    ) {

    }

    /**
     * 그룹 탈퇴
     */
    @DeleteMapping("/withdraw")
    public void withdrawTeam(
            @RequestParam("member_id") Long memberId
    ) {

    }
}
