package com.onerty.yeogi.exception;

import org.springframework.http.HttpStatus;

public enum ErrorType {

    TERMS_NOT_FOUND("a0001", HttpStatus.NOT_FOUND, "저장된 약관 내역이 없습니다"),
    SMS_AUTH_MISSING_REQUIRED_FIELD("a0002", HttpStatus.BAD_REQUEST, "sms 인증 필수 입력값이 누락되었습니다"),
    SMS_AUTH_FAILED("a0003", HttpStatus.UNAUTHORIZED, "sms 인증에 실패했습니다"),
    INVALID_PASSWORD_FORMAT("a0004", HttpStatus.BAD_REQUEST, "비밀번호 형식이 올바르지 않습니다"),
    INVALID_EMAIL_FORMAT("a0005", HttpStatus.BAD_REQUEST, "이메일 형식이 올바르지 않습니다"),
    DUPLICATE_NICKNAME("a0006", HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다"),
    DUPLICATE_EMAIL("a0007", HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다"),


    INTERNAL_SERVER_ERROR("common", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류");

    private String code;
    private HttpStatus httpStatus;
    private String message;

    ErrorType(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
