package com.onerty.yeogi.customer.reservation;

import com.onerty.yeogi.common.exception.ErrorType;
import com.onerty.yeogi.common.exception.YeogiException;
import com.onerty.yeogi.common.room.*;
import com.onerty.yeogi.common.room.enums.ReservationStatus;
import com.onerty.yeogi.common.room.enums.RoomStatus;
import com.onerty.yeogi.common.user.User;
import com.onerty.yeogi.customer.reservation.dto.CreateReservationRequest;
import com.onerty.yeogi.customer.reservation.dto.CreateReservationResponse;
import com.onerty.yeogi.customer.room.RoomRepository;
import com.onerty.yeogi.customer.room.RoomTypeRepository;
import com.onerty.yeogi.customer.room.RoomTypeStockRepository;
import com.onerty.yeogi.customer.user.UserRepository;
import com.onerty.yeogi.customer.utils.DistributedLockExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomTypeStockRepository stockRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final DistributedLockExecutor lockExecutor;

    public CreateReservationResponse reserveRoom(CreateReservationRequest req) {
        RoomType roomType = roomTypeRepository.findById(req.roomTypeId())
                .orElseThrow(() -> new YeogiException(ErrorType.ROOM_TYPE_NOT_FOUND));

        List<LocalDate> dates = req.checkIn().datesUntil(req.checkOut()).toList();

        for (LocalDate date : dates) {
            String lockKey = "lock:stock:" + roomType.getId() + ":" + date;

            lockExecutor.executeWithLock(lockKey, 3, 10, () -> {
                RoomTypeDateId dateId = new RoomTypeDateId(roomType.getId(), date);
                RoomTypeStock stock = stockRepository.findById(dateId)
                        .orElseThrow(() -> new YeogiException(ErrorType.ROOM_STOCK_NOT_FOUND));

                if (stock.getStock() <= 0) {
                    throw new YeogiException(ErrorType.ROOM_STOCK_NOT_FOUND);
                }

                stock.setStock(stock.getStock() - 1);
                stockRepository.save(stock);

                return null;
            });
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

