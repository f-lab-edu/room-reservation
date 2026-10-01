package com.onerty.yeogi.customer.auth.dto;

public record JwtPayload(
        Long userId,
        String userIdentifier
) {
}
