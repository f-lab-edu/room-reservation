package com.onerty.yeogi.host.room.dto;

import java.util.List;

public record CreateRoomTypeRequest(
        String name,
        int capacity,
        int pricePerNight,
        String description,
        List<CreateRoomRequest> rooms
) {
}
