package com.onerty.yeogi.host.room.dto;

public record CreateRoomRequest(
        String roomNumber,
        String floor
) {
}
