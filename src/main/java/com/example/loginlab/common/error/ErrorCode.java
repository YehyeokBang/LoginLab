package com.example.loginlab.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // DUPLICATE
    DUPLICATE_USER_EMAIL(HttpStatus.BAD_REQUEST, "D001", "이미 사용 중인 이메일입니다."),
    DUPLICATE_USER_NICKNAME(HttpStatus.BAD_REQUEST, "D002", "이미 사용 중인 닉네임입니다."),

    // INVALID
    INVALID_EMAIL_PASSWORD_MATCH(HttpStatus.UNAUTHORIZED, "I001", "이메일과 비밀번호가 일치하지 않습니다.");

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;

}
