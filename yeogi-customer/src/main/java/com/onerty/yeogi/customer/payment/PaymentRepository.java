package com.onerty.yeogi.customer.payment;

import com.onerty.yeogi.common.room.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
