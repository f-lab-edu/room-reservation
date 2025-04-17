
package com.onerty.yeogi.customer.room;


import com.onerty.yeogi.customer.room.dto.SearchAccommodationRequest;
import com.onerty.yeogi.customer.room.dto.SearchAccommodationResponse;
import com.onerty.yeogi.customer.room.dto.SearchAccommodationRoomRequest;
import com.onerty.yeogi.customer.room.dto.SearchAccommodationRoomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/page")
    public Page<SearchAccommodationResponse> searchPage(
            SearchAccommodationRequest req, Pageable pageable
    ) {
        return roomService.searchAccommodationsPage(req, pageable);
    }

    @GetMapping("/scroll")
    public List<SearchAccommodationResponse> searchScroll(
            SearchAccommodationRequest req,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") int size
    ) {
        return roomService.searchAccommodationsCursor(req, lastId, size);
    }

    @GetMapping("/{accommodationId}/rooms")
    public List<SearchAccommodationRoomResponse> searchRooms(
            @ModelAttribute SearchAccommodationRoomRequest req
    ) {
        return roomService.searchAvailableRoomsByAccommodation(req);
    }
}

