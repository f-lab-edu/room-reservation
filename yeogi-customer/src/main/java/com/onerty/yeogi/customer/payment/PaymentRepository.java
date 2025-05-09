package com.onerty.yeogi.customer.payment;

import com.onerty.yeogi.common.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
