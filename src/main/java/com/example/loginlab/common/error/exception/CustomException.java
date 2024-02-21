package com.example.loginlab.common.error.exception;

import com.example.loginlab.common.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    ErrorCode errorCode;
}
