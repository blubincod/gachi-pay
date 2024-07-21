package com.gachi.gachipay.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Account
    USER_NOT_FOUND("존재하지 않는 사용자입니다."),
    ACCOUNT_NOT_FOUND("계좌가 없습니다."),
    ACCOUNT_EXISTS("이미 등록된 계좌입니다."),
    MAX_ACCOUNT_PER_USER_3("사용자 최대 계좌는 3개입니다."),
    USER_ACCOUNT_UN_MATCH("사용자와 계좌의 소유자가 다릅니다."),
    ACCOUNT_ALREADY_UNREGISTERED("이미 해지된 계좌입니다."),
    BALANCE_NOT_EMPTY("잔액이 있는 계좌는 해지할 수 없습니다."),
    // Transaction
    LACK_BALANCE("잔액이 부족합니다."),
    TRANSACTION_NOT_FOUND("해당 거래가 없습니다."),
    AMOUNT_EXCEED_BALANCE("금액이 잔액을 초과했습니다."),
    ACCOUNT_TRANSACTION_LOCK("해당 계좌는 사용 중입니다."),
    TRANSACTION_ACCOUNT_UN_MATCH("이 거래는 해당 계좌에서 발생한 거래가 아닙니다."),
    CANCEL_MUST_FULLY("부분 취소는 허용되지 않습니다."),
    TOO_OLD_ORDER_TO_CANCEL("6개월이 지난 거래는 취소가 불가능합니다."),
    // Team
    TEAM_NOT_FOUND("해당 그룹이 존재하지 않습니다."),
    NOT_TEAM_MEMBER("그룹의 멤버가 아닙니다."),
    REPRESENTATIVE_CANNOT_LEAVE("대표자는 그룹을 탈퇴할 수 없습니다."),
    // Other
    INTERNAL_SERVER_ERROR("내부 서버 오류가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다.");

    private String description;
}
