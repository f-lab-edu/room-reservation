package com.onerty.yeogi.host.room;

import com.onerty.yeogi.common.room.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomGenerationService {

    private final RoomRepository roomRepository;
    private final RoomTypeStockRepository stockRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final ActualRoomRepository actualRoomRepository;

    public void generateNextMonthRoomAndStock() {
        LocalDate now = LocalDate.now();
        YearMonth nextMonth = YearMonth.from(now.plusMonths(1));
        LocalDate startDate = nextMonth.atDay(1);
        LocalDate endDate = nextMonth.atEndOfMonth();

        List<RoomType> allRoomTypes = roomTypeRepository.findAll();

        for (RoomType roomType : allRoomTypes) {
            List<ActualRoom> actualRooms = actualRoomRepository.findByRoomType(roomType);

            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

                // Room 생성
                for (int i = 0; i < actualRooms.size(); i++) {
                    Room room = Room.builder()
                            .roomType(roomType)
                            .date(date)
                            .status(Room.RoomStatus.AVAILABLE)
                            .build();
                    roomRepository.save(room);
                }

                // RoomTypeStock 생성
                RoomTypeStock stock = RoomTypeStock.builder()
                        .id(new RoomTypeDateId(roomType.getId(), date))
                        .stock(actualRooms.size())
                        .build();
                stockRepository.save(stock);
            }
        }
    }
}
