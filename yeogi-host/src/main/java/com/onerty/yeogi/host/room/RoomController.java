package com.onerty.yeogi.host.room;

import com.onerty.yeogi.common.room.Accommodation;
import com.onerty.yeogi.common.util.BaseResponse;
import com.onerty.yeogi.common.util.MessageResponse;
import com.onerty.yeogi.host.room.dto.CreateAccommodationRequest;
import com.onerty.yeogi.host.room.dto.CreateAccommodationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomGenerationService roomGenerationService;

    @PostMapping("/accommodation")
    public BaseResponse<CreateAccommodationResponse> registerAccommodation(@RequestBody CreateAccommodationRequest request) {
        CreateAccommodationResponse response = roomService.createAccommodation(request);
        return new BaseResponse.success<>(response);
    }

    @PostMapping("/generate-next-month")
    public BaseResponse<MessageResponse> generateNextMonthRoomAndStock() {
        roomGenerationService.generateNextMonthRoomAndStock();
        return new BaseResponse.success<>(new MessageResponse("Next month's rooms and stock generated!"));
    }

}

