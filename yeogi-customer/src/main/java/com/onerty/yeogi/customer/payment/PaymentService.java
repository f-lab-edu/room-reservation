package com.onerty.yeogi.customer.payment;

import com.onerty.yeogi.common.exception.ErrorType;
import com.onerty.yeogi.common.exception.YeogiException;
import com.onerty.yeogi.common.payment.Payment;
import com.onerty.yeogi.common.reservation.Reservation;
import com.onerty.yeogi.common.reservation.TempReservation;
import com.onerty.yeogi.common.room.*;
import com.onerty.yeogi.common.room.enums.PaymentStatus;
import com.onerty.yeogi.common.room.enums.ReservationStatus;
import com.onerty.yeogi.common.room.enums.RoomStatus;
import com.onerty.yeogi.common.user.User;
import com.onerty.yeogi.customer.payment.dto.*;
import com.onerty.yeogi.customer.reservation.ReservationRepository;
import com.onerty.yeogi.customer.reservation.TempReservationRepository;
import com.onerty.yeogi.customer.room.RoomTypeRepository;
import com.onerty.yeogi.customer.room.RoomTypeStockRepository;
import com.onerty.yeogi.customer.user.UserRepository;
import com.onerty.yeogi.customer.utils.DistributedLockExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final RoomTypeStockRepository stockRepository;
    private final TempReservationRepository tempReservationRepository;
    private final UserRepository userRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RedisTemplate<String, String> redisTemplate;

//    🧩 initiatePayment 결제 초기 데이터 생성
//    🧩 /mock-pg/pay 에서 completePayment 호출
//    🧩 completePayment 금액 검증 및 결제 상태 변경
    public CreatePaymentResponse initiatePayment(CreatePaymentRequest request) {
        Reservation reservation = reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new YeogiException(ErrorType.RESERVATION_NOT_FOUND));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new YeogiException(ErrorType.RESERVATION_ALREADY_PAID);
        }

        Payment payment = Payment.builder()
                .reservation(reservation)
                .amount(reservation.getTotalPrice())
                .status(PaymentStatus.PENDING)
                .build();
        Payment saved = paymentRepository.save(payment);

        return new CreatePaymentResponse(saved.getId(), saved.getAmount(), null, reservation.getId());
    }

    public CompletePaymentResponse completePayment(CompletePaymentRequest request) {
        TempReservation temp = tempReservationRepository.findById(request.tempReservationId())
                .orElseThrow(() -> new YeogiException(ErrorType.RESERVATION_NOT_FOUND));

        if (temp.getTotalPrice() != request.paidAmount()) {
            throw new YeogiException(ErrorType.RESERVATION_AMOUNT_MISMATCH);
        }

        User user = userRepository.findById(temp.getUserId())
                .orElseThrow(() -> new YeogiException(ErrorType.USER_NOT_FOUND));

        RoomType roomType = roomTypeRepository.findById(temp.getRoomTypeId())
                .orElseThrow(() -> new YeogiException(ErrorType.ROOM_TYPE_NOT_FOUND));

        Reservation reservation = Reservation.builder()
                .user(user)
                .roomType(roomType)
                .checkIn(temp.getCheckIn())
                .checkOut(temp.getCheckOut())
                .guestCount(temp.getGuestCount())
                .totalPrice(temp.getTotalPrice())
                .status(ReservationStatus.RESERVED)
                .build();

        Reservation saved = reservationRepository.save(reservation);

        Payment payment = Payment.builder()
                .reservation(saved)
                .amount(temp.getTotalPrice())
                .status(PaymentStatus.COMPLETED)
                .paidAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        // 가예약 삭제 + Redis TTL 제거
        tempReservationRepository.deleteById(temp.getId());
        redisTemplate.delete("reserve:temp:" + temp.getId());

        return new CompletePaymentResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getPaidAt(),
                saved.getId()
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

        // 재고 복구
        LocalDate start = reservation.getCheckIn();
        LocalDate end = reservation.getCheckOut();
        Long roomTypeId = reservation.getRoomType().getId();

        for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
            RoomTypeDateId dateId = new RoomTypeDateId(roomTypeId, date);
            RoomTypeStock stock = stockRepository.findById(dateId)
                    .orElseThrow(() -> new YeogiException(ErrorType.STOCK_NOT_FOUND));
            stock.setStock(stock.getStock() + 1);
        }

        // Room 상태 복구
        for (Room room : reservation.getRooms()) {
            room.setStatus(RoomStatus.AVAILABLE);
            room.setReservation(null); // 연관관계 해제 (optional, orphanRemoval 아닐 경우)
        }

        return new CancelPaymentResponse(req.reservationId());
    }

}