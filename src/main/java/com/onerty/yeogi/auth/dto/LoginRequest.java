package com.onerty.yeogi.auth.dto;

import lombok.Getter;

public record LoginRequest(
        String email,
        String password
) {
}
