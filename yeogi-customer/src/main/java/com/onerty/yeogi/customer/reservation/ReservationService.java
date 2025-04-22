package com.onerty.yeogi.customer.reservation;

import com.onerty.yeogi.common.exception.ErrorType;
import com.onerty.yeogi.common.exception.YeogiException;
import com.onerty.yeogi.common.room.Reservation;
import com.onerty.yeogi.common.room.RoomType;
import com.onerty.yeogi.common.room.RoomTypeDateId;
import com.onerty.yeogi.common.room.RoomTypeStock;
import com.onerty.yeogi.common.room.enums.ReservationStatus;
import com.onerty.yeogi.common.user.User;
import com.onerty.yeogi.customer.reservation.dto.CreateReservationRequest;
import com.onerty.yeogi.customer.reservation.dto.CreateReservationResponse;
import com.onerty.yeogi.customer.room.RoomTypeRepository;
import com.onerty.yeogi.customer.room.RoomTypeStockRepository;
import com.onerty.yeogi.customer.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomTypeStockRepository stockRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final UserRepository userRepository;

    public CreateReservationResponse reserveRoom(CreateReservationRequest req) {
        RoomType roomType = roomTypeRepository.findById(req.roomTypeId())
                .orElseThrow(() -> new YeogiException(ErrorType.ROOM_TYPE_NOT_FOUND));

        // 재고 확인 및 차감
        for (LocalDate date = req.checkIn(); date.isBefore(req.checkOut()); date = date.plusDays(1)) {
            RoomTypeDateId dateId = new RoomTypeDateId(req.roomTypeId(), date);
            RoomTypeStock stock = stockRepository.findById(dateId)
                    .orElseThrow(() -> new YeogiException(ErrorType.ROOM_STOCK_NOT_FOUND));

            if (stock.getStock() <= 0) {
                throw new YeogiException(ErrorType.ROOM_STOCK_EMPTY);
            }

            stock.setStock(stock.getStock() - 1);
        }

        int nights = (int) ChronoUnit.DAYS.between(req.checkIn(), req.checkOut());
        int totalPrice = nights * roomType.getPricePerNight();

        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> new YeogiException(ErrorType.USER_NOT_FOUND));

        Reservation reservation = Reservation.builder()
                .user(user)
                .checkIn(req.checkIn())
                .checkOut(req.checkOut())
                .guestCount(req.guestCount())
                .status(ReservationStatus.PENDING)
                .roomType(roomType)
                .totalPrice(totalPrice)
                .build();

        Reservation saved = reservationRepository.save(reservation);

        return new CreateReservationResponse(
                saved.getId(),
                saved.getCheckIn(),
                saved.getCheckOut(),
                saved.getGuestCount(),
                saved.getTotalPrice(),
                saved.getStatus().name(),
                saved.getRoomType().getId(),
                saved.getUser().getUserId()
        );
    }
}

