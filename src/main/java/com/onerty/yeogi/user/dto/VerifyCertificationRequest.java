package com.onerty.yeogi.user.dto;

public record VerifyCertificationRequest(
        String phoneNumber,
        String certificationNumber
) {
}
