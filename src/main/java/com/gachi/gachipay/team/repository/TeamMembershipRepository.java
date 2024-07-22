package com.gachi.gachipay.team.repository;

import com.gachi.gachipay.member.entity.Member;
import com.gachi.gachipay.team.entity.Team;
import com.gachi.gachipay.team.entity.TeamMembership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamMembershipRepository extends JpaRepository<TeamMembership, Long> {
    /**
     * 그룹의 멤버 정보 찾기
     */
    TeamMembership findByTeamAndMember(Team team, Member member);

    /**
     * 그룹에 속한 멤버 삭제
     * 내부적인 트랜잭션 처리 불가로 삭제 쿼리를 직접 작성
     */
    void deleteByTeamIdAndMemberId(Long teamId, Long memberId);

    Optional<TeamMembership> findByMemberIdAndTeamId(Long memberId, Long teamId);
}
