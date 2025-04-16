package com.onerty.yeogi.host.room.dto;

import java.util.List;

public record CreateAccommodationRequest(
        String name,
        String location,
        Long hostId,
        List<CreateRoomTypeRequest> roomTypes
) {
}
