package com.onerty.yeogi.customer.reservation;

import com.onerty.yeogi.common.util.BaseResponse;
import com.onerty.yeogi.customer.reservation.dto.CreateReservationRequest;
import com.onerty.yeogi.customer.reservation.dto.CreateReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("")
    public BaseResponse<CreateReservationResponse> reserve(@RequestBody CreateReservationRequest request){
        CreateReservationResponse response = reservationService.reserveRoom(request);
        return new BaseResponse.success<>(response);
    }
}
