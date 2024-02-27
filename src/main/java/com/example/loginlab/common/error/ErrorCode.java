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

    // NOT_FOUND
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "N001", "존재하지 않는 사용자입니다."),

    // INVALID
    INVALID_EMAIL_PASSWORD_MATCH(HttpStatus.UNAUTHORIZED, "I001", "이메일과 비밀번호가 일치하지 않습니다."),
    INVALID_CERTIFICATION_CODE(HttpStatus.BAD_REQUEST, "I002", "인증 코드가 일치하지 않습니다."),

    // FAILED
    FAILED_TO_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "F001", "이메일 전송에 실패했습니다. (재시도 및 관리자 문의)"),

    // UNAUTHORIZED
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "U001", "로그인이 필요합니다."),

    // FORBIDDEN
    FORBIDDEN_USER_LEVEL(HttpStatus.FORBIDDEN, "F001", "접근 권한이 없습니다.");

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;

}
