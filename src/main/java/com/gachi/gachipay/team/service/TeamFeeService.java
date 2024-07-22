package com.gachi.gachipay.team.service;

import com.gachi.gachipay.account.repository.AccountRepository;
import com.gachi.gachipay.member.repository.MemberRepository;
import com.gachi.gachipay.team.entity.Team;
import com.gachi.gachipay.team.entity.TeamMemberStatus;
import com.gachi.gachipay.team.entity.TeamMembership;
import com.gachi.gachipay.team.repository.TeamMembershipRepository;
import com.gachi.gachipay.team.repository.TeamRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 자동 회비 납부 관련 서비스
 */
@Slf4j
@Service
@AllArgsConstructor
public class TeamFeeService {
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final TeamRepository teamRepository;
    private final TeamMembershipRepository teamMembershipRepository;

    /**
     * 활성화된 모든 그룹 가져오기
     */
    public List<Team> getActiveTeams() {
        return teamRepository.findAll();
    }

    /**
     * 활성화된 그룹의 멤버들 가져오기
     */
    public List<TeamMembership> getActiveMembers(Team team){
        return teamMembershipRepository.findByTeamIdAndStatus(team.getId(), TeamMemberStatus.ACTIVE);
    }
}
