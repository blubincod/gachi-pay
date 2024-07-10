package com.gachi.gachipay.common.exception;

import lombok.*;

@Data
@AllArgsConstructor
public class AccountException extends RuntimeException {
    ErrorCode errorCode;
    String errorMessage;
}
