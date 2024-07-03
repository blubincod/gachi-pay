package com.gachi.gachipay.global.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountException extends RuntimeException {
    ErrorCode errorCode;
    String errorMessage;
}
