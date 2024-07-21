package com.gachi.gachipay.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamException extends RuntimeException {
    ErrorCode errorCode;
    String errorMessage;

    public TeamException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = getErrorMessage();
    }
}
