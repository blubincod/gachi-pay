package com.gachi.gachipay.transaction.service;

import com.gachi.gachipay.account.entity.Account;
import com.gachi.gachipay.account.entity.AccountStatus;
import com.gachi.gachipay.account.repository.AccountRepository;
import com.gachi.gachipay.common.exception.AccountException;
import com.gachi.gachipay.common.exception.ErrorCode;
import com.gachi.gachipay.common.exception.TeamException;
import com.gachi.gachipay.member.entity.Member;
import com.gachi.gachipay.member.repository.MemberRepository;
import com.gachi.gachipay.team.entity.Team;
import com.gachi.gachipay.team.entity.TeamMemberStatus;
import com.gachi.gachipay.team.entity.TeamMembership;
import com.gachi.gachipay.team.repository.TeamMembershipRepository;
import com.gachi.gachipay.team.repository.TeamRepository;
import com.gachi.gachipay.transaction.entity.Transaction;
import com.gachi.gachipay.transaction.entity.TransactionResultType;
import com.gachi.gachipay.transaction.entity.TransactionType;
import com.gachi.gachipay.transaction.model.TransactionDto;
import com.gachi.gachipay.transaction.repository.TransactionRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.gachi.gachipay.transaction.entity.TransactionResultType.FAILURE;
import static com.gachi.gachipay.transaction.entity.TransactionResultType.SUCCESS;
import static com.gachi.gachipay.transaction.entity.TransactionType.CANCEL;
import static com.gachi.gachipay.transaction.entity.TransactionType.USE;

@Slf4j
@Builder
@RequiredArgsConstructor
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final TeamRepository teamRepository;
    private final TeamMembershipRepository teamMembershipRepository;

    /**
     * 잔액 사용
     */
    public TransactionDto useBalance(Long memberId, String accountNumber, Long amount) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AccountException(
                        ErrorCode.USER_NOT_FOUND));
        Account account = getAccount(accountNumber);

        validateUseBalance(member, account, amount); // 유효성 검사 메서드 호출

        account.useBalance(amount); // 잔액 부족 검사 메서드 호출

        Transaction transaction = saveAndGetTransaction(USE, SUCCESS, account, amount);

        return TransactionDto.fromEntity(transaction);
    }

    /**
     * 유효성 검사
     * 1. 계좌 소유자 일치 확인
     * 2. 계좌 상태 확인(사용중인 계좌 / 해지된 계좌)
     * 3. 잔액 부족 확인
     */
    private void validateUseBalance(Member member, Account account, Long amount) {
        // 계좌 소유자 일치 확인
        if (member.getId() != account.getMember().getId()) {
            throw new AccountException(
                    ErrorCode.USER_ACCOUNT_UN_MATCH);
        }

        // 계좌 상태 확인
        if (account.getStatus() != AccountStatus.IN_USE) {
            throw new AccountException(
                    ErrorCode.ACCOUNT_ALREADY_UNREGISTERED);
        }

        // 잔액 부족 확인
        if (account.getBalance() < amount) {
            throw new AccountException(
                    ErrorCode.LACK_BALANCE);
        }
    }

    /**
     * 잔액 사용 실패
     */
    public void saveFailedUseTransaction(String accountNumber, Long amount) {
        Account account = getAccount(accountNumber);

        saveAndGetTransaction(USE, FAILURE, account, amount);
    }

    /**
     * 잔액 사용 취소
     */
    public TransactionDto cancelBalance(String transactionId, String accountNumber, Long amount) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new AccountException(
                        ErrorCode.TRANSACTION_NOT_FOUND));

        Account account = getAccount(accountNumber);

        validateCancelBalance(transaction, account, amount); // 유효성 검사 메서드 호출

        account.cancelBalance(amount); // 거래 취소 금액 확인 후 처리

        transaction = saveAndGetTransaction(CANCEL, SUCCESS, account, amount);

        return TransactionDto.fromEntity(transaction);
    }

    /**
     * 잔액 사용 취소 유효성 검사
     * 1. 해당 계좌에서의 거래인지 확인
     * 2. 부분 취소는 취소 불가
     * 3. 6개월이 지난 거래 취소 불가
     */
    private void validateCancelBalance(Transaction transaction, Account account, Long amount) {
        if (transaction.getAccount().getId() != account.getMember().getId()) {
            throw new AccountException(
                    ErrorCode.TRANSACTION_ACCOUNT_MISMATCH);
        }
        if (!transaction.getAmount().equals(amount)) {
            throw new AccountException(
                    ErrorCode.CANCEL_AMOUNT_MISMATCH);
        }
        if (transaction.getTransactedAt().isBefore(LocalDateTime.now().minusMonths(6))) {
            throw new AccountException(
                    ErrorCode.TOO_OLD_ORDER_TO_CANCEL);
        }
    }

    /**
     * 잔액 사용 취소 실패
     */
    public void saveFailedCancelTransaction(String accountNumber, Long amount) {
        Account account = getAccount(accountNumber);

        saveAndGetTransaction(CANCEL, FAILURE, account, amount);
    }

    /**
     * 개인 거래 내역 저장 및 반환
     */
    private Transaction saveAndGetTransaction(TransactionType transactionType,
                                              TransactionResultType transactionResultType,
                                              Account account,
                                              Long amount) {
        return transactionRepository.save(
                Transaction.builder()
                        .transactionId(UUID.randomUUID().toString().replace("-", ""))
                        .transactionType(transactionType)
                        .transactionResult(transactionResultType)
                        .account(account)
                        .amount(amount)
                        .balanceSnapshot(account.getBalance())
                        .transactedAt(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * 거래 정보 조회
     */
    public TransactionDto queryTransaction(String transactionId) {
        return TransactionDto.fromEntity(transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new AccountException(
                        ErrorCode.TRANSACTION_NOT_FOUND)));
    }

    /**
     * 그룹 잔액 사용
     */
    public TransactionDto useTeamBalance(Long teamId, Long memberId, Long amount) {
        Team team = getTeam(teamId);

        //FIXME 사용하지 않는 변수
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AccountException(
                        ErrorCode.USER_NOT_FOUND));

        Account account = team.getRepresentativeAccount();

        validateUseTeamBalance(team, memberId, account, amount);

        account.useBalance(amount);

        Transaction transaction = saveAndGetTeamTransaction(
                USE, SUCCESS, account, amount, memberId, team);

        return TransactionDto.fromEntity(transaction);
    }

    /**
     * 그룹 잔액 사용 유효성 검사
     * 1. 그룹의 멤버인지 확인
     * 2. 탈퇴한 멤버인지 확인
     * 3. 대표 계좌 확인
     * 4. 계좌 상태 확인
     * 5. 잔액으로 거래 금액을 처리 가능한지 확인
     */
    private void validateUseTeamBalance(Team team, Long memberId, Account account, Long amount) {
        TeamMembership membership =  teamMembershipRepository.findByMemberIdAndTeamId(memberId, team.getId())
                .orElseThrow(() -> new TeamException(ErrorCode.NOT_TEAM_MEMBER));

        if (membership.getStatus() != TeamMemberStatus.ACTIVE) {
            throw new TeamException(ErrorCode.WITHDRAWN_MEMBER);
        }

        if (account == null || !account.equals(team.getRepresentativeAccount())) {
            throw new AccountException(ErrorCode.INVALID_REPRESENTATIVE_ACCOUNT);
        }

        if (account.getStatus() != AccountStatus.IN_USE) {
            throw new AccountException(ErrorCode.ACCOUNT_ALREADY_UNREGISTERED);
        }

        if (account.getBalance() < amount) {
            throw new AccountException(ErrorCode.LACK_BALANCE);
        }
    }

    /**
     * 그룹 잔액 사용 실패
     */
    public void saveFailedUseTeamTransaction(Long teamId, Long memberId, Long amount) {
        Team team = getTeam(teamId);

        Account account = team.getRepresentativeAccount();

        saveAndGetTeamTransaction(USE, FAILURE, account, amount, memberId, team);
    }

    /**
     * 그룹 잔액 사용 취소
     */
    public TransactionDto cancelTeamBalance(Long teamId, String transactionId, Long amount) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new AccountException(ErrorCode.TRANSACTION_NOT_FOUND));

        Team team = getTeam(teamId);
        Account representativeAccount = team.getRepresentativeAccount();

        validateCancelTeamBalance(transaction, team, representativeAccount, amount);

        representativeAccount.cancelBalance(amount);

        Transaction cancelTransaction = saveAndGetTeamTransaction(
                CANCEL, SUCCESS, representativeAccount, amount, transaction.getMemberId(), team);

        return TransactionDto.fromEntity(cancelTransaction);
    }

    /**
     * 그룹 잔액 사용 취소 유효성 검사
     */
    private void validateCancelTeamBalance(Transaction transaction, Team team, Account account, Long amount) {
        if (!transaction.getAccount().equals(account)) {
            throw new AccountException(ErrorCode.TRANSACTION_ACCOUNT_MISMATCH);
        }
        if (!transaction.getAmount().equals(amount)) {
            throw new AccountException(ErrorCode.CANCEL_AMOUNT_MISMATCH);
        }
        if (transaction.getTransactedAt().isBefore(LocalDateTime.now().minusMonths(6))) {
            throw new AccountException(ErrorCode.TOO_OLD_ORDER_TO_CANCEL);
        }
    }

    /**
     * 그룹 잔액 사용 취소 실패
     */
    public void saveFailedCancelTeamTransaction(Long teamId, String transactionId) {
        Team team = getTeam(teamId);

        Account account = team.getRepresentativeAccount();

        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new AccountException(ErrorCode.TRANSACTION_NOT_FOUND));

        saveAndGetTeamTransaction(
                CANCEL, FAILURE, account, transaction.getAmount(), transaction.getMemberId(), team);
    }

    /**
     * 그룹 거래 내역 저장 및 반환
     */
    private Transaction saveAndGetTeamTransaction(TransactionType transactionType,
                                                  TransactionResultType transactionResultType,
                                                  Account account,
                                                  Long amount,
                                                  Long memberId,
                                                  Team team) {
        return transactionRepository.save(
                Transaction.builder()
                        .transactionId(UUID.randomUUID().toString().replace("-", ""))
                        .transactionType(transactionType)
                        .transactionResult(transactionResultType)
                        .account(account)
                        .amount(amount)
                        .memberId(memberId)
                        .team(team)
                        .balanceSnapshot(account.getBalance())
                        .transactedAt(LocalDateTime.now())
                        .memberId(memberId)
                        .team(team)
                        .build()
        );
    }

    // 계좌 정보 조회
    private Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(
                        ErrorCode.ACCOUNT_NOT_FOUND));
    }

    // 팀 정보 가져오기
    private Team getTeam(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new AccountException(ErrorCode.TEAM_NOT_FOUND));
    }
}
