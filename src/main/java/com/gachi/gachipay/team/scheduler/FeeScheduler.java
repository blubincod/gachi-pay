package com.gachi.gachipay.team.scheduler;

import com.gachi.gachipay.account.entity.Account;
import com.gachi.gachipay.account.service.AccountService;
import com.gachi.gachipay.common.exception.AccountException;
import com.gachi.gachipay.team.entity.Team;
import com.gachi.gachipay.team.entity.TeamMembership;
import com.gachi.gachipay.team.service.TeamFeeService;
import com.gachi.gachipay.team.service.TeamService;
import com.gachi.gachipay.transaction.service.TeamFeeTransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class FeeScheduler {
    private final TeamService teamService;
    private final TeamFeeService teamFeeService;
    private final AccountService accountService;
    private final TeamFeeTransactionService teamFeeTransactionService;

    /**
     * 매월 15일 자정마다 회비를 걷는 스케쥴러
     * 한적한 시간인 매월 15일 새벽 2시에 실행
     */
//    @Scheduled(cron = "0 0 2 15 * ?")
    @Scheduled(fixedRate = 10000) // 10초 간격으로 테스트
    public void monthlyFeeScheduling() {
        LocalDate today = LocalDate.now();
        log.info("Starting Monthly fee collection check for date: {}", today);

        List<Team> activeTeams = teamFeeService.getActiveTeams();

        for (Team team : activeTeams) {
                collectFeeForTeam(team);
        }

        log.info("Monthly fee collection completed.");
    }

    /**
     * 그룹과 각 멤버에 대한 회비 수집이 개별적인 트랜잭션으로 처리하여
     * 한 멤버의 회비 수집 실패가 다른 멤버나 다른 팀의 회비 수집에 영향을 미치지 않기 때문에
     * 개별 계좌의 잔액 부족으로 인한 오류가 발생해도 전체 스케줄링 작업이 중단되지 않고 계속 진행 가능
     */
    @Transactional // 원자적으로 수행하기 위함
    public void collectFeeForTeam(Team team) {
        Long monthlyFee = team.getMonthlyFee(); // 월 회비
        Account representativeAccount = team.getRepresentativeAccount(); // 그룹의 대표 계좌

        // 그룹에 활동 중인 멤버 리스트
        List<TeamMembership> activeMembers = teamFeeService.getActiveMembers(team);

        // 활동 중인 각 멤버의 계좌에서 송금
        for (TeamMembership membership : activeMembers) {
            Account memberAccount = membership.getAccount();

            try {
                teamFeeTransactionService.transferMoneyToTeamAccount(team, memberAccount);
                log.info("Successfully collected {} from member {} for team {}",
                        monthlyFee, membership.getMember().getId(), team.getId());
            } catch (AccountException e) { // 송금 실패 시 거래 실패 내역 저장
                log.error("Failed to collect fee from member {} for team {}: Lack Balance",
                        membership.getMember().getId(), team.getId());
                teamFeeTransactionService.saveFailedTeamFeeTransaction(team, memberAccount);
            } catch (Exception e) { // 첫 번째 catch 블록에서 처리되지 않은 다른 모든 예외를 처리
                log.error("Unexpected error occurred while collecting fee from member {} for team {}: {}",
                        membership.getMember().getId(), team.getId(), e.getMessage());
            }
        }
    }
}
