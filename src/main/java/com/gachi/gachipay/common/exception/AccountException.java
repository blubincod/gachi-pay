package com.gachi.gachipay.common.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountException extends RuntimeException {
    ErrorCode errorCode;
    String errorMessage;

    public AccountException(ErrorCode errorCode) {
    }
}
