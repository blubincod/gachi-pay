package com.gachi.gachipay.team.service;

import com.gachi.gachipay.account.entity.Account;
import com.gachi.gachipay.account.repository.AccountRepository;
import com.gachi.gachipay.common.exception.AccountException;
import com.gachi.gachipay.common.exception.ErrorCode;
import com.gachi.gachipay.common.exception.TeamException;
import com.gachi.gachipay.member.entity.Member;
import com.gachi.gachipay.member.repository.MemberRepository;
import com.gachi.gachipay.team.entity.Team;
import com.gachi.gachipay.team.entity.TeamMembership;
import com.gachi.gachipay.team.model.CreateTeam;
import com.gachi.gachipay.team.model.TeamDto;
import com.gachi.gachipay.team.repository.TeamMembershipRepository;
import com.gachi.gachipay.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 그룹 서비스
 * 그룹 생성 및 삭제
 * 그룹 정보 조회
 * 그룹 가입 및 해제
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final TeamRepository teamRepository;
    private final TeamMembershipRepository teamMembershipRepository;

    /**
     * 그룹 생성
     */
    public Team createTeam(
            CreateTeam.Request request
    ) {
        Member member = getMember(request.getMemberId());

        return teamRepository.save(
                Team.builder()
                        .representativeId(member.getId())
                        .teamName(request.getTeamName())
                        .description(request.getDescription())
                        .maxMembers(request.getMaxMembers())
                        .monthlyFee(request.getMonthlyFee())
                        .build());
    }

    /**
     * 그룹 삭제
     * 그룹의 대표만 삭제 가능
     * 삭제 시 돈이 남아 있다면 자동 분배(10원 단위)
     */
    public void deleteTeam(Long teamId, Long memberId) {
        Team team = getTeam(teamId);

        // 그룹의 대표 여부 확인
        if (team.getRepresentativeId() != memberId) {
            throw new TeamException(ErrorCode.REPRESENTATIVE_ONLY_DELETE_TEAM);
        }

        // TODO 잔액이 남아 있다면 잔액 분배

        teamRepository.deleteById(teamId);
    }

    /**
     * 그룹 정보 확인
     * TODO 페이지네이션
     */
    public TeamDto getTeamInfo(Long teamId) {
        Team team = getTeam(teamId);

        return TeamDto.fromEntity(team);
    }

    /**
     * 그룹 목록 조회
     */
    public List<TeamDto> getTeams() {

        List<Team> teams = teamRepository.findAll();

        return teams.stream()
                .map(TeamDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 그룹 가입
     * 등록된 계좌가 하나 이상 존재 시 가입 가능
     */
    public TeamMembership joinTeam(Long teamId, Long memberId) {
        Member member = getMember(memberId);

        Team team = getTeam(teamId);

        return teamMembershipRepository.save(team.addMember(member));
    }


    /**
     * 그룹 탈퇴
     * 해당 그룹의 대표는 탈퇴 불가
     */
    @Transactional
    public void withdrawTeam(Long teamId, Long memberId) {
        Member member = getMember(memberId);

        Team team = getTeam(teamId);

        // 그룹에 해당 멤버가 있는지 확인
        boolean isMember = teamMembershipRepository.existsByTeamAndMember(team, member);
        if (!isMember) {
            throw new TeamException(ErrorCode.NOT_TEAM_MEMBER);
        }

        // 대표자인 경우 탈퇴 불가
        if (team.getRepresentativeId().equals(memberId)) {
            throw new TeamException(ErrorCode.REPRESENTATIVE_CANNOT_LEAVE);
        }

        teamMembershipRepository.deleteByTeamIdAndMemberId(teamId, memberId);
    }

    /**
     * 자동 회비 납부
     */

    // 회원 정보 가져오기
    private Member getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AccountException(ErrorCode.USER_NOT_FOUND));
        return member;
    }

    // 계좌 정보 가져오기
    private Account getAccount(String accountNumber) {
        System.out.println(accountNumber);
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(
                        ErrorCode.ACCOUNT_NOT_FOUND));
        return account;
    }

    // 그룹 정보 가져오기
    private Team getTeam(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(ErrorCode.TEAM_NOT_FOUND));
    }


}
