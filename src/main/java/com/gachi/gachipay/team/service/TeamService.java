package com.gachi.gachipay.team.service;

import com.gachi.gachipay.account.entity.Account;
import com.gachi.gachipay.account.repository.AccountRepository;
import com.gachi.gachipay.common.exception.AccountException;
import com.gachi.gachipay.common.exception.ErrorCode;
import com.gachi.gachipay.common.exception.TeamException;
import com.gachi.gachipay.member.entity.Member;
import com.gachi.gachipay.member.repository.MemberRepository;
import com.gachi.gachipay.team.entity.Team;
import com.gachi.gachipay.team.entity.TeamMemberRole;
import com.gachi.gachipay.team.entity.TeamMemberStatus;
import com.gachi.gachipay.team.entity.TeamMembership;
import com.gachi.gachipay.team.model.CreateTeam;
import com.gachi.gachipay.team.model.TeamDto;
import com.gachi.gachipay.team.repository.TeamMembershipRepository;
import com.gachi.gachipay.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public CreateTeam.Response createTeam(
            CreateTeam.Request request
    ) {
        Member member = getMember(request.getMemberId());
        Account account = getAccount(request.getAccountNumber());
        Team team = teamRepository.save(
                Team.builder()
                        .representativeId(member.getId())
                        .representativeAccount(account)
                        .teamName(request.getTeamName())
                        .description(request.getDescription())
                        .maxMembers(request.getMaxMembers())
                        .monthlyFee(request.getMonthlyFee())
                        .build());

        // 이미 그룹의 대표로 등록된 계좌인지 확인
        if (account.isRepresentativeAccount() || account.getTeam() != null) {
            throw new TeamException(ErrorCode.REPRESENTATIVE_ACCOUNT_ALREADY_REGISTERED);
        }

        // 그룹 계좌 여부 및 생성된 team을 그룹 대표의 계좌(Account) 레코드에 설정
        account.setRepresentativeAccount(true);
        account.setTeam(team);
        accountRepository.save(account);

        // 그룹 생성 시 Membership에 대표 회원으로 추가
        TeamMembership membership = team.addMember(member);
        membership.setAccount(account);
        membership.setRole(TeamMemberRole.REPRESENTATIVE);
        membership.setStatus(TeamMemberStatus.ACTIVE);
        teamMembershipRepository.save(membership);

        TeamDto teamDto = TeamDto.fromEntity(team);

        return CreateTeam.Response.fromDto(teamDto);
    }

    /**
     * 그룹 삭제
     * 그룹의 대표만 삭제 가능
     * // TODO 잔액이 남아 있다면 잔액 분배 (10원 단위)
     */
    public void deleteTeam(Long teamId, Long memberId, String accountNumber) {
        Team team = getTeam(teamId);

        // 그룹의 대표 여부 확인
        if (team.getRepresentativeId() != memberId) {
            throw new TeamException(ErrorCode.REPRESENTATIVE_ONLY_DELETE_TEAM);
        }

        Account account = getAccount(accountNumber);

        // 그룹 계좌 여부 및 생성된 team을 그룹 대표의 계좌(Account) 레코드에 설정
        account.setRepresentativeAccount(false);
        account.setTeam(null);
        accountRepository.save(account);

        teamRepository.deleteById(teamId);
    }

    /**
     * 그룹 정보 확인
     */
    public TeamDto getTeamInfo(Long teamId) {
        Team team = getTeam(teamId);

        return TeamDto.fromEntity(team);
    }

    /**
     * 그룹 목록 조회
     * TODO 페이지네이션
     */
    public List<TeamDto> getTeams() {

        List<Team> teams = teamRepository.findAll();

        return teams.stream()
                .map(TeamDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 그룹 가입
     * 대표자는 자신이 생성한 그룹 가입 불가
     * 등록된 계좌가 하나 이상 존재 시 가입 가능
     */
    public TeamMembership joinTeam(Long teamId, Long memberId, String accountNumber) {
        Member member = getMember(memberId);

        Team team = getTeam(teamId);

        Account account = getAccount(accountNumber);

        TeamMembership teamMembership = team.addMember(member);

        teamMembership.setAccount(account);
        teamMembership.setRole(TeamMemberRole.MEMBER);
        teamMembership.setStatus(TeamMemberStatus.ACTIVE);

        return teamMembershipRepository.save(teamMembership);
    }


    /**
     * 그룹 탈퇴
     * 해당 그룹의 대표는 탈퇴 불가
     */
    @Transactional
    public void withdrawTeam(Long teamId, Long memberId) {
        Member member = getMember(memberId);

        Team team = getTeam(teamId);

        // 현재 그룹에 해당 멤버가 있는지 확인
        TeamMembership teamMembership = teamMembershipRepository.findByTeamAndMember(team, member);
        if (teamMembership == null) {
            throw new TeamException(ErrorCode.NOT_TEAM_MEMBER);
        }

        // 대표자인 경우 탈퇴 불가
        if (team.getRepresentativeId().equals(memberId)) {
            throw new TeamException(ErrorCode.REPRESENTATIVE_CANNOT_LEAVE);
        }

        teamMembership.setStatus(TeamMemberStatus.WITHDRAWN);
        teamMembership.setLeftAt(LocalDateTime.now());
        teamMembershipRepository.save(teamMembership);
    }

    // 회원 정보 가져오기
    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new AccountException(ErrorCode.USER_NOT_FOUND));
    }

    // 계좌 정보 가져오기
    public Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(
                        ErrorCode.ACCOUNT_NOT_FOUND));
    }

    // 그룹 정보 가져오기
    public Team getTeam(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(ErrorCode.TEAM_NOT_FOUND));
    }
}
