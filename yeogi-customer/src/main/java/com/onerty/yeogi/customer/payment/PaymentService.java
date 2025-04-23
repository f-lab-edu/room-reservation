package com.onerty.yeogi.customer.payment;

import com.onerty.yeogi.common.exception.ErrorType;
import com.onerty.yeogi.common.exception.YeogiException;
import com.onerty.yeogi.common.room.*;
import com.onerty.yeogi.common.room.enums.PaymentStatus;
import com.onerty.yeogi.common.room.enums.ReservationStatus;
import com.onerty.yeogi.common.room.enums.RoomStatus;
import com.onerty.yeogi.customer.reservation.ReservationRepository;
import com.onerty.yeogi.customer.payment.dto.CancelPaymentRequest;
import com.onerty.yeogi.customer.payment.dto.CancelPaymentResponse;
import com.onerty.yeogi.customer.payment.dto.CreatePaymentRequest;
import com.onerty.yeogi.customer.payment.dto.CreatePaymentResponse;
import com.onerty.yeogi.customer.room.RoomTypeStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final RoomTypeStockRepository stockRepository;

    public CreatePaymentResponse pay(CreatePaymentRequest req) {
        Reservation reservation = reservationRepository.findById(req.reservationId())
                .orElseThrow(() -> new YeogiException(ErrorType.RESERVATION_NOT_FOUND));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new YeogiException(ErrorType.RESERVATION_ALREADY_PAID);
        }

        if (reservation.getTotalPrice() != req.amount()) {
            Payment failedPayment = Payment.builder()
                    .reservation(reservation)
                    .amount(req.amount())
                    .status(PaymentStatus.FAILED)
                    .paidAt(LocalDateTime.now())
                    .build();
            paymentRepository.save(failedPayment);

            throw new YeogiException(ErrorType.RESERVATION_AMOUNT_MISMATCH);
        }

        Payment payment = Payment.builder()
                .reservation(reservation)
                .amount(req.amount())
                .status(PaymentStatus.COMPLETED)
                .paidAt(LocalDateTime.now())
                .build();

        reservation.setStatus(ReservationStatus.RESERVED);
        Payment saved = paymentRepository.save(payment);

        return new CreatePaymentResponse(
                saved.getId(),
                saved.getAmount(),
                saved.getPaidAt(),
                saved.getReservation().getId()
        );
    }

    public CancelPaymentResponse cancelPayment(CancelPaymentRequest req) {
        Reservation reservation = reservationRepository.findById(req.reservationId())
                .orElseThrow(() -> new YeogiException(ErrorType.RESERVATION_NOT_FOUND));

        if (reservation.getStatus() != ReservationStatus.RESERVED) {
            throw new YeogiException(ErrorType.RESERVATION_NOT_PAYABLE);
        }

        Payment payment = reservation.getPayment();
        if (payment == null || payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new YeogiException(ErrorType.PAYMENT_NOT_FOUND_OR_ALREADY_CANCELED);
        }

        payment.setStatus(PaymentStatus.CANCELED);
        reservation.setStatus(ReservationStatus.CANCELED);

        // ✅ 재고 복구
        LocalDate start = reservation.getCheckIn();
        LocalDate end = reservation.getCheckOut();
        Long roomTypeId = reservation.getRoomType().getId();

        for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
            RoomTypeDateId dateId = new RoomTypeDateId(roomTypeId, date);
            RoomTypeStock stock = stockRepository.findById(dateId)
                    .orElseThrow(() -> new YeogiException(ErrorType.STOCK_NOT_FOUND));
            stock.setStock(stock.getStock() + 1);
        }

        // ✅ Room 상태 복구
        for (Room room : reservation.getRooms()) {
            room.setStatus(RoomStatus.AVAILABLE);
            room.setReservation(null); // 연관관계 해제 (optional, orphanRemoval 아닐 경우)
        }

        return new CancelPaymentResponse(req.reservationId());
    }

}