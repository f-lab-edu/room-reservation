package com.onerty.yeogi.host.room;

import com.onerty.yeogi.common.room.Accommodation;
import com.onerty.yeogi.host.room.dto.CreateAccommodationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/accommodation")
    public ResponseEntity<?> registerAccommodation(@RequestBody CreateAccommodationRequest request) {
        Accommodation accommodation = roomService.createAccommodation(request);
        return ResponseEntity.ok(accommodation.getId());
    }

}

