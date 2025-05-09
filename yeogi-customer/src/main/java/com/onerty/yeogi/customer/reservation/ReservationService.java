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

        List<Room> roomsToReserve = dates.stream()
                .map(date -> roomRepository.findFirstByRoomTypeAndDateAndStatus(roomType, date, RoomStatus.AVAILABLE)
                        .orElseThrow(() -> new YeogiException(ErrorType.ROOM_STOCK_NOT_FOUND))
                )
                .sorted(Comparator.comparing(Room::getId))
                .toList();

        List<Room> reservedRooms = new ArrayList<>();

        for (Room room : roomsToReserve) {
            String lockKey = "lock:room:" + room.getId();
            LocalDate date = room.getDate();

            lockExecutor.executeWithLock(lockKey, 3, 10, () -> {
                if (room.getStatus() != RoomStatus.AVAILABLE) {
                    throw new YeogiException(ErrorType.ROOM_STOCK_NOT_FOUND);
                }

                room.setStatus(RoomStatus.RESERVED);
                reservedRooms.add(room);

                RoomTypeDateId dateId = new RoomTypeDateId(req.roomTypeId(), date);
                RoomTypeStock stock = stockRepository.findById(dateId)
                        .orElseThrow(() -> new YeogiException(ErrorType.ROOM_STOCK_NOT_FOUND));
                stock.setStock(stock.getStock() - 1);

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

        // 양방향 연관관계 설정
        for (Room room : reservedRooms) {
            room.setReservation(reservation);
            reservation.getRooms().add(room);
        }

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

