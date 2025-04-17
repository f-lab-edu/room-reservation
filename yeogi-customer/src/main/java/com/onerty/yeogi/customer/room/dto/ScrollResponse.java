package com.onerty.yeogi.customer.room.dto;

import java.util.List;

public record ScrollResponse<T>(
        List<T> content,
        Long lastId,      // 리스트에서 마지막 ID
        boolean hasNext   // 다음 페이지가 있는지 여부
) {
}
