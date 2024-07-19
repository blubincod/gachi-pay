package com.gachi.gachipay.account.model;

import lombok.*;

public class RegisterAccount {
    @Getter
    @Setter
    public static class Request {
        private Long initialBalance; // 초기 잔액
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long initialBalance; // 초기 잔액
    }

}
