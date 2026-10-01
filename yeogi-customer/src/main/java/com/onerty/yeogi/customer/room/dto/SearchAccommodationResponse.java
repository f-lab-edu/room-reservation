package com.onerty.yeogi.customer.room.dto;

public record SearchAccommodationResponse(
        String name,
        String location,
        Integer lowestPrice // 남아있는 숙소 재고에 대해 가장 싼 가격
) {
}