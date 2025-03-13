package com.onerty.yeogi.customer.user.dto;

public record VerifyCertificationRequest(
        String phoneNumber,
        String certificationNumber
) {
}
