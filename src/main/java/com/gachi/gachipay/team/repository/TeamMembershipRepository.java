package com.gachi.gachipay.team.repository;

import com.gachi.gachipay.member.entity.Member;
import com.gachi.gachipay.team.entity.Team;
import com.gachi.gachipay.team.entity.TeamMembership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMembershipRepository extends JpaRepository<TeamMembership, Long> {
    /**
     * 그룹과 멤버의 존재 확인
     */
    boolean existsByTeamAndMember(Team team, Member member);

    /**
     * 그룹에 속한 멤버 삭제
     * 내부적인 트랜잭션 처리 불가로 삭제 쿼리를 직접 작성
     */
//    @Modifying
//    @Query(
//            "DELETE FROM TeamMembership tm " +
//                    "WHERE tm.team.id = :teamId AND tm.member.id = :memberId")
    void deleteByTeamIdAndMemberId(Long teamId, Long memberId);
}
