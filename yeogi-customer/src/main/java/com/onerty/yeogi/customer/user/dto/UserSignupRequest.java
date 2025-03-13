package com.onerty.yeogi.customer.user.dto;

import com.onerty.yeogi.customer.exception.YeogiException;
import com.onerty.yeogi.customer.exception.ErrorType;
import com.onerty.yeogi.customer.util.Checkable;
import io.micrometer.common.util.StringUtils;

import java.util.List;

import static com.onerty.yeogi.customer.util.validation.ValidationPatterns.*;

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

    @Override
    public void check() {

        if (StringUtils.isBlank(nick) || StringUtils.isBlank(phoneNumber)) {
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
