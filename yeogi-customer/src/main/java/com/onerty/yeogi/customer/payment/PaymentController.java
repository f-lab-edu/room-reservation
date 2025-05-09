package com.onerty.yeogi.customer.payment;

import com.onerty.yeogi.common.util.BaseResponse;
import com.onerty.yeogi.customer.payment.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/initiate")
    public BaseResponse<CreatePaymentResponse>  payment (@RequestBody CreatePaymentRequest request){
        CreatePaymentResponse response = paymentService.initiatePayment(request);
        return new BaseResponse.success<>(response);
    }

    @PostMapping("/callback")
    public BaseResponse<CompletePaymentResponse>  payment (@RequestBody CompletePaymentRequest request){
        CompletePaymentResponse response = paymentService.completePayment(request);
        return new BaseResponse.success<>(response);
    }

    @DeleteMapping
    public BaseResponse<CancelPaymentResponse> cancelPayment (@RequestBody CancelPaymentRequest request){
        CancelPaymentResponse response = paymentService.cancelPayment(request);
        return new BaseResponse.success<>(response);
    }
}
