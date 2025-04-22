package com.onerty.yeogi.customer.reservation.dto;

import java.time.LocalDate;

public record CreateReservationResponse(
        Long reservationId,
        LocalDate checkIn,
        LocalDate checkOut,
        int guestCount,
        int totalPrice,
        String status,
        Long roomTypeId,
        Long userId
) {
}
