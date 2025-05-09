package com.onerty.yeogi.customer.payment;

import com.onerty.yeogi.common.util.MessageResponse;
import com.onerty.yeogi.customer.payment.dto.CompletePaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mock-pg")
public class MockPgController {
    private final PaymentService paymentService;

    @PostMapping("/pay")
    public MessageResponse mockPayment(@RequestBody CompletePaymentRequest request) {
        paymentService.completePayment(request);
        return new MessageResponse("✅모의 결제 완료 -> 콜백 호출로 이어짐");
    }
}
