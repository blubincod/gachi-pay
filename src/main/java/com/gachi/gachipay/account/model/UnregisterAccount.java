package com.gachi.gachipay.account.model;

import lombok.*;

public class UnregisterAccount {
    @Getter
    @Setter
    public static class Request {
        private Long userId; //계좌 소유자 아이디
        private String accountNumber; //계좌 번호
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long initialBalance; //초기 잔액
    }

}
