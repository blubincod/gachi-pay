package com.gachi.gachipay.team.controller;

import com.gachi.gachipay.team.entity.TeamMembership;
import com.gachi.gachipay.team.model.CreateTeam;
import com.gachi.gachipay.team.model.TeamDto;
import com.gachi.gachipay.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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
    public ResponseEntity<CreateTeam.Response> createTeam(
            @RequestBody @Valid CreateTeam.Request request
    ) {
        CreateTeam.Response response = teamService.createTeam(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getTeamId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    /**
     * 그룹 정보 조회
     */
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDto> getTeamInfo(
            @PathVariable Long teamId
    ) {
        TeamDto teamInfo = teamService.getTeamInfo(teamId);

        return ResponseEntity.ok(teamInfo);
    }

    /**
     * 그룹 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<TeamDto>> getTeams(
    ) {
        List<TeamDto> teams = teamService.getTeams();

        return ResponseEntity.ok(teams);
    }

    /**
     * 그룹 삭제
     */
    @DeleteMapping("/{teamId}/members")
    public ResponseEntity<?> deleteTeam(
            @PathVariable Long teamId,
            @RequestParam("member_id") Long memberId,
            @RequestParam("account_number") String accountNumber
    ) {
        teamService.deleteTeam(teamId, memberId, accountNumber);

        return ResponseEntity.noContent().build();
    }

    /**
     * 그룹 가입
     */
    @PostMapping("/{teamId}/members")
    public ResponseEntity<TeamMembership> joinTeam(
            @PathVariable Long teamId,
            @RequestParam("member_id") Long memberId
    ) {
        TeamMembership teamMembership = teamService.joinTeam(teamId, memberId);

        return ResponseEntity.ok(teamMembership);
    }

    /**
     * 그룹 탈퇴
     */
    @DeleteMapping("/{teamId}/members/{memberId}")
    public ResponseEntity<?> withdrawTeam(
            @PathVariable Long teamId,
            @PathVariable Long memberId
    ) {
        teamService.withdrawTeam(teamId, memberId);

        return ResponseEntity.noContent().build();
    }
}
