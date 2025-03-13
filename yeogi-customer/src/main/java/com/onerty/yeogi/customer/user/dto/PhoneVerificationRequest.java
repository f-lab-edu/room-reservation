package com.onerty.yeogi.customer.user.dto;

import com.onerty.yeogi.customer.exception.ErrorType;
import com.onerty.yeogi.customer.exception.YeogiException;
import com.onerty.yeogi.customer.util.Checkable;

public record PhoneVerificationRequest(
        String phoneNumber,
        String certificationCode
) implements Checkable {

    @Override
    public void check() {
        if (phoneNumber == null || certificationCode == null) {
            throw new YeogiException(ErrorType.SMS_AUTH_MISSING_REQUIRED_FIELD);
        }
    }
}
