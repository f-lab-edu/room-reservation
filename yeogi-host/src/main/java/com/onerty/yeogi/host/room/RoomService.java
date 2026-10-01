package com.onerty.yeogi.host.room;

import com.onerty.yeogi.common.exception.ErrorType;
import com.onerty.yeogi.common.exception.YeogiException;
import com.onerty.yeogi.common.room.*;
import com.onerty.yeogi.common.room.enums.RoomStatus;
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
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

        List<RoomType> savedRoomTypes = new ArrayList<>();

        for (CreateRoomTypeRequest roomTypeReq : request.roomTypes()) {
            RoomType roomType = RoomType.builder()
                    .name(roomTypeReq.name())
                    .capacity(roomTypeReq.capacity())
                    .pricePerNight(roomTypeReq.pricePerNight())
                    .description(roomTypeReq.description())
                    .accommodation(accommodation)
                    .build();

            roomTypeRepository.save(roomType);
            savedRoomTypes.add(roomType);

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
        }

        List<RoomTypeCountProjection> roomCounts = actualRoomRepository
                .countByRoomTypeInAccommodation(accommodation.getId());
        Map<Long, Long> roomCountMap = roomCounts.stream()
                .collect(Collectors.toMap(RoomTypeCountProjection::getRoomTypeId, RoomTypeCountProjection::getCount));

        for (RoomType roomType : savedRoomTypes) {
            long count = roomCountMap.getOrDefault(roomType.getId(), 0L);
            generateRooms(roomType, count);
            updateStock(roomType, count);
        }

        return new CreateAccommodationResponse(accommodation.getId());
    }

    public void generateRooms(RoomType roomType, long count) {
        LocalDate today = LocalDate.now();
        YearMonth nextMonth = YearMonth.now().plusMonths(1);
        LocalDate end = nextMonth.atEndOfMonth();

        List<Room> rooms = new ArrayList<>();

        for (LocalDate date = today; !date.isAfter(end); date = date.plusDays(1)) {
            if (roomRepository.existsByRoomTypeAndDate(roomType, date)) continue;

            for (int i = 0; i < count; i++) {
                rooms.add(Room.builder()
                        .id(UUID.randomUUID().toString())
                        .roomType(roomType)
                        .date(date)
                        .status(RoomStatus.AVAILABLE)
                        .build());
            }
        }

        roomRepository.saveAll(rooms);
    }

    public void updateStock(RoomType roomType, long count) {
        LocalDate today = LocalDate.now();
        YearMonth nextMonth = YearMonth.now().plusMonths(1);
        LocalDate end = nextMonth.atEndOfMonth();

        List<RoomTypeStock> stocks = new ArrayList<>();

        for (LocalDate date = today; !date.isAfter(end); date = date.plusDays(1)) {
            RoomTypeDateId id = new RoomTypeDateId(roomType.getId(), date);
            if (stockRepository.existsById(id)) continue;

            stocks.add(RoomTypeStock.builder()
                    .id(id)
                    .stock((int) count)
                    .build());
        }

        stockRepository.saveAll(stocks);
    }

}