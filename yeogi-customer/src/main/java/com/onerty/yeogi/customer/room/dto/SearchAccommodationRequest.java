package com.onerty.yeogi.customer.room.dto;

import java.time.LocalDate;

public record SearchAccommodationRequest(
        String location,
        LocalDate checkIn,
        LocalDate checkOut,
        int guestCount
) {
}

