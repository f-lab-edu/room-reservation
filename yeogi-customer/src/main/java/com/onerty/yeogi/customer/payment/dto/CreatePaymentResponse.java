package com.onerty.yeogi.customer.payment.dto;

import java.time.LocalDateTime;

public record CreatePaymentResponse(
        Long id,
        int amount,
        LocalDateTime paidAt,
        Long reservationId
) {
}
