package com.gachi.gachipay.common.exception;

import lombok.*;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private ErrorCode errorCode;
    private String ErrorMessage;

}
