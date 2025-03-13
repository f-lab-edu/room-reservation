package com.onerty.yeogi.customer.util.validation;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@Getter
@UtilityClass
public class ValidationPatterns {
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    public static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
}
