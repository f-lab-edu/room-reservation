package com.onerty.yeogi.customer.payment;

import com.onerty.yeogi.common.util.BaseResponse;
import com.onerty.yeogi.customer.payment.dto.CancelPaymentRequest;
import com.onerty.yeogi.customer.payment.dto.CancelPaymentResponse;
import com.onerty.yeogi.customer.payment.dto.CreatePaymentRequest;
import com.onerty.yeogi.customer.payment.dto.CreatePaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public BaseResponse<CreatePaymentResponse> pay (@RequestBody CreatePaymentRequest request){
        CreatePaymentResponse response = paymentService.pay(request);
        return new BaseResponse.success<>(response);
    }

    @DeleteMapping
    public BaseResponse<CancelPaymentResponse> cancelPayment (@RequestBody CancelPaymentRequest request){
        CancelPaymentResponse response = paymentService.cancelPayment(request);
        return new BaseResponse.success<>(response);
    }
}
