package com.gachi.gachipay.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND("사용자가 없습니다."),
    ACCOUNT_NOT_FOUND("계좌가 없습니다."),
    ACCOUNT_EXISTS("이미 등록된 계좌입니다."),
    MAX_ACCOUNT_PER_USER_3("사용자 최대 계좌는 3개입니다."),
    INTERNAL_SERVER_ERROR("내부 서버 오류가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다."),
    AMOUNT_EXCEED_BALANCE("해당 거래가 없습니다."),
    TRANSACTION_ACCOUNT_UN_MATCH("이 거래는 해당 계좌에서 발생한 거래가 아닙니다."),
    TRANSACTION_NOT_FOUND("거래 금액이 계좌 잔액보다 큽니다."),
    ACCOUNT_TRANSACTION_LOCK("해당 계좌는 사용 중입니다."),
    LACK_BALANCE("작액이 부족합니다."),
    CANCEL_MUST_FULLY("부분 취소는 허용되지 않습니다."),
    TOO_OLD_ORDER_TO_CANCEL("6개월이 지난 거래는 취소가 불가능합니다."),
    USER_ACCOUNT_UN_MATCH("사용자와 계좌의 소유자가 다릅니다."),
    ACCOUNT_ALREADY_UNREGISTERED("이미 해지된 계좌입니다."),
    BALANCE_NOT_EMPTY("잔액이 있는 계좌는 해지할 수 없습니다.");

    private String description;
}
