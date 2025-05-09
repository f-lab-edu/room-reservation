package com.onerty.yeogi.customer.reservation;

import com.onerty.yeogi.common.util.BaseResponse;
import com.onerty.yeogi.customer.reservation.dto.CreateReservationRequest;
import com.onerty.yeogi.customer.reservation.dto.CreateReservationResponse;
import com.onerty.yeogi.customer.security.CustomerUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public BaseResponse<CreateReservationResponse> reserve(
            @RequestBody CreateReservationRequest request,
            @AuthenticationPrincipal CustomerUserDetails userDetails
    ){
        CreateReservationResponse response = reservationService.reserveRoom(request, userDetails.getUserId());
        return new BaseResponse.success<>(response);
    }
}
