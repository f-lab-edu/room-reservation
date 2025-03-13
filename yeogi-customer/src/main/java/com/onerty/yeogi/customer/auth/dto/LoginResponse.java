package com.onerty.yeogi.customer.auth.dto;

import com.onerty.yeogi.customer.user.User;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        User user
) {
}
