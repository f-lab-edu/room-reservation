package com.onerty.yeogi.user.dto;

import com.onerty.yeogi.exception.YeogiException;
import com.onerty.yeogi.exception.ErrorType;
import com.onerty.yeogi.util.Checkable;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public record UserSignupRequest(
        int signupType,
        String nick,
        String phoneNumber,
        String gender,
        String birth,
        List<UserTermsAgreementStatus> agreements,
        String uid,
        String upw
) implements Checkable {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");

    @Override
    public void check() {

        if (nick == null || nick.isBlank() || phoneNumber == null || phoneNumber.isBlank()) {
            throw new YeogiException(ErrorType.SIGNUP_MISSING_REQUIRED_FIELD);
        }

        if (uid == null || !EMAIL_PATTERN.matcher(uid).matches()) {
            throw new YeogiException(ErrorType.INVALID_EMAIL_FORMAT);
        }

        if (upw == null || !PASSWORD_PATTERN.matcher(upw).matches()) {
            throw new YeogiException(ErrorType.INVALID_PASSWORD_FORMAT);
        }
    }
}
