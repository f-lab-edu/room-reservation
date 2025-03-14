package com.onerty.yeogi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    TERMS_NOT_FOUND("a0001", HttpStatus.NOT_FOUND, "저장된 약관 내역이 없습니다"),
    SMS_AUTH_MISSING_REQUIRED_FIELD("a0002", HttpStatus.BAD_REQUEST, "sms 인증 필수 입력값이 누락되었습니다"),
    SMS_AUTH_FAILED("a0003", HttpStatus.UNAUTHORIZED, "sms 인증에 실패했습니다"),
    INVALID_PASSWORD_FORMAT("a0004", HttpStatus.BAD_REQUEST, "비밀번호 형식이 올바르지 않습니다"),
    INVALID_EMAIL_FORMAT("a0005", HttpStatus.BAD_REQUEST, "이메일 형식이 올바르지 않습니다"),
    DUPLICATE_NICKNAME("a0006", HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다"),
    DUPLICATE_EMAIL("a0007", HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다"),
    SIGNUP_MISSING_REQUIRED_FIELD("a0008", HttpStatus.BAD_REQUEST, "회원가입 필수 입력값이 누락되었습니다"),
    SIGNUP_INVALID_TERM_ID("a0009", HttpStatus.BAD_REQUEST, "유효하지 않은 약관 ID입니다."),
    SIGNUP_REQUIRED_TERMS_NOT_ACCEPTED("a0010", HttpStatus.BAD_REQUEST, "필수 약관에 동의하지 않았습니다."),

    USER_NOT_FOUND("b0001", HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다"),
    INVALID_PASSWORD("b0002", HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다"),
    INVALID_ACCESS_TOKEN("b0003", HttpStatus.UNAUTHORIZED, "유효하지 않은 액세스 토큰입니다"),
    INVALID_REFRESH_TOKEN("b0004", HttpStatus.FORBIDDEN, "유효하지 않은 리프레시 토큰입니다"),
    EXPIRED_ACCESS_TOKEN("b0005", HttpStatus.UNAUTHORIZED, "만료된 액세스 토큰입니다"),
    EXPIRED_REFRESH_TOKEN("b0006", HttpStatus.FORBIDDEN, "만료된 리프레시 토큰입니다"),
    LOGGED_OUT_TOKEN("b0007", HttpStatus.UNAUTHORIZED, "로그아웃된 토큰입니다"),

    ALREADY_EXIST_TERM("t0001", HttpStatus.CONFLICT, "이미 존재하는 약관입니다."),
    TERM_NOT_FOUND("t0002", HttpStatus.NOT_FOUND, "해당 약관을 찾을 수 없습니다."),
    TERM_MISSING_FIELD("t0003", HttpStatus.BAD_REQUEST, "약관 생성 필수 입력값이 누락되었습니다"),
    INVALID_TERM_TITLE("t0004", HttpStatus.FORBIDDEN, "유효하지 않은 약관 제목입니다."),

    INTERNAL_SERVER_ERROR("common", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류");

    private String code;
    private HttpStatus httpStatus;
    private String message;
}
