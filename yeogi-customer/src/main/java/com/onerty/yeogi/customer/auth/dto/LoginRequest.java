package com.onerty.yeogi.customer.auth.dto;

public record LoginRequest(
        String email,
        String password
) {
}
