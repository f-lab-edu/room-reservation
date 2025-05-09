package com.onerty.yeogi.customer.reservation.dto;

import java.time.LocalDate;

public record CreateReservationRequest(
        Long roomTypeId,
        LocalDate checkIn,
        LocalDate checkOut,
        int guestCount
) {
}
