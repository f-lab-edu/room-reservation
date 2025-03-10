package com.onerty.yeogi.auth.dto;

import com.onerty.yeogi.user.User;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        User user
) {
}
