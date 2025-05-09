package com.onerty.yeogi.customer.payment.dto;

public record CompletePaymentRequest(
        Long reservationId,
        int paidAmount
) {
}
