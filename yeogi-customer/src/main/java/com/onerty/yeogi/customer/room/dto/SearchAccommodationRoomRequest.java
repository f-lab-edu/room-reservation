package com.onerty.yeogi.customer.room.dto;

import java.time.LocalDate;

public record SearchAccommodationRoomRequest(
        Long AccommodationId,
        LocalDate checkIn,
        LocalDate checkOut,
        int guestCount
) {

}