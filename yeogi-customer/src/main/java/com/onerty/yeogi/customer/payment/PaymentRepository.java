package com.onerty.yeogi.customer.payment;

import com.onerty.yeogi.common.payment.Payment;
import com.onerty.yeogi.common.room.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByTempReservationId(Long id);

   Optional<Payment> findByTempReservationIdAndStatus(Long id, PaymentStatus paymentStatus);
}
