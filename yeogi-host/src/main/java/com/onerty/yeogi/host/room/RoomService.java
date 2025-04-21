package com.onerty.yeogi.host.room;

import com.onerty.yeogi.common.exception.ErrorType;
import com.onerty.yeogi.common.exception.YeogiException;
import com.onerty.yeogi.common.room.*;
import com.onerty.yeogi.common.user.Host;
import com.onerty.yeogi.host.room.dto.CreateAccommodationRequest;
import com.onerty.yeogi.host.room.dto.CreateAccommodationResponse;
import com.onerty.yeogi.host.room.dto.CreateRoomRequest;
import com.onerty.yeogi.host.room.dto.CreateRoomTypeRequest;
import com.onerty.yeogi.host.user.HostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final HostRepository hostRepository;
    private final AccommodationRepository accommodationRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final ActualRoomRepository actualRoomRepository;
    private final RoomRepository roomRepository;
    private final RoomTypeStockRepository stockRepository;

    public CreateAccommodationResponse createAccommodation(CreateAccommodationRequest request) {
        Host host = hostRepository.findById(request.hostId())
                .orElseThrow(() -> new YeogiException(ErrorType.HOST_NOT_FOUND));

        Accommodation accommodation = Accommodation.builder()
                .name(request.name())
                .location(request.location())
                .host(host)
                .build();

        accommodationRepository.save(accommodation);

        for (CreateRoomTypeRequest roomTypeReq : request.roomTypes()) {
            RoomType roomType = RoomType.builder()
                    .name(roomTypeReq.name())
                    .capacity(roomTypeReq.capacity())
                    .pricePerNight(roomTypeReq.pricePerNight())
                    .description(roomTypeReq.description())
                    .accommodation(accommodation)
                    .build();

            roomTypeRepository.save(roomType);

            if (roomTypeReq.rooms() != null) {
                for (CreateRoomRequest roomReq : roomTypeReq.rooms()) {
                    ActualRoom actualRoom = ActualRoom.builder()
                            .roomNumber(roomReq.roomNumber())
                            .floor(roomReq.floor())
                            .roomType(roomType)
                            .build();
                    actualRoomRepository.save(actualRoom);
                }
            }

            generateRooms(roomType);
            updateStock(roomType);
        }

        return new CreateAccommodationResponse(
                accommodation.getId()
        );
    }

    public void generateRooms(RoomType roomType) {
        List<ActualRoom> actualRooms = actualRoomRepository.findByRoomType(roomType);

        LocalDate today = LocalDate.now();
        YearMonth nextMonth = YearMonth.now().plusMonths(1);
        LocalDate end = nextMonth.atEndOfMonth();

        List<Room> rooms = new ArrayList<>();

        for (LocalDate date = today; !date.isAfter(end); date = date.plusDays(1)) {
            if (roomRepository.existsByRoomTypeAndDate(roomType, date)) continue; // 멱등성

            for (ActualRoom ar : actualRooms) {
                rooms.add(Room.builder()
                        .id(UUID.randomUUID().toString())
                        .roomType(roomType)
                        .date(date)
                        .roomNumber(ar.getRoomNumber())
                        .floor(ar.getFloor())
                        .status(Room.RoomStatus.AVAILABLE)
                        .build());
            }
        }

        roomRepository.saveAll(rooms);
    }

    public void updateStock(RoomType roomType) {
        LocalDate today = LocalDate.now();
        YearMonth nextMonth = YearMonth.now().plusMonths(1);
        LocalDate end = nextMonth.atEndOfMonth();

        List<RoomTypeStock> stocks = new ArrayList<>();

        for (LocalDate date = today; !date.isAfter(end); date = date.plusDays(1)) {
            RoomTypeDateId id = new RoomTypeDateId(roomType.getId(), date);
            if (stockRepository.existsById(id)) continue;

            int count = roomRepository.countByRoomTypeAndDate(roomType, date);

            stocks.add(RoomTypeStock.builder()
                    .id(id)
                    .stock(count)
                    .build());
        }

        stockRepository.saveAll(stocks);
    }

}