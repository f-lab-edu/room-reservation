package com.onerty.yeogi.customer.room.dto;

public record SearchAccommodationRoomResponse(
        String roomName,
        int capacity,
        int price, // pricePerNight * (숙박 일수)
        String description
) {
}