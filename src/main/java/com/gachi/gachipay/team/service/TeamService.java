package com.gachi.gachipay.team.service;

import com.gachi.gachipay.account.entity.Account;
import com.gachi.gachipay.account.repository.AccountRepository;
import com.gachi.gachipay.common.exception.AccountException;
import com.gachi.gachipay.common.exception.ErrorCode;
import com.gachi.gachipay.member.entity.Member;
import com.gachi.gachipay.member.repository.MemberRepository;
import com.gachi.gachipay.team.entity.Team;
import com.gachi.gachipay.team.model.CreateTeam;
import com.gachi.gachipay.team.model.TeamDto;
import com.gachi.gachipay.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    /**
     * 그룹 생성
     */
    public Team createTeam(
            CreateTeam.Request request
    ) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new AccountException(ErrorCode.USER_NOT_FOUND));

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
            throw new RuntimeException("그룹 삭제 권한은 그룹 대표에게만 있습니다.");
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
     * 그룹 가입
     */
    public void joinTeam() {

    }

    /**
     * 그룹 탈퇴
     */
    public void withdrawTeam() {

    }

    /**
     * 자동 회비 납부
     *
     */

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
                .orElseThrow(() -> new RuntimeException("존재하지 않는 그룹입니다."));
    }
}
