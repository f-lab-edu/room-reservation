package com.onerty.yeogi.customer.user;

import com.onerty.yeogi.customer.auth.dto.JwtPayload;
import com.onerty.yeogi.customer.user.dto.UserSignupRequest;
import com.onerty.yeogi.common.user.User;

// yeogi-customer 모듈 내의 UserMapper 클래스
public class UserMapper {
    public static User from(UserSignupRequest dto) {
        return User.builder()
                .userType(dto.signupType())
                .nickname(dto.nick())
                .phoneNumber(dto.phoneNumber())
                .gender(dto.gender())
                .birthDate(dto.birth())
                .userIdentifier(dto.uid())
                .userPassword(dto.upw())
                .build();
    }

    public static JwtPayload toJwtPayload(User user) {
        return new JwtPayload(user.getUserId(), user.getUserIdentifier());
    }
}
