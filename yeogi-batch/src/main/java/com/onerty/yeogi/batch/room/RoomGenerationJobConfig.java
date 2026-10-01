package com.onerty.yeogi.batch.room;

import com.onerty.yeogi.batch.room.repository.*;
import com.onerty.yeogi.batch.room.repository.projections.RoomTypeIdAccommodationIdProjection;
import com.onerty.yeogi.common.room.*;
import com.onerty.yeogi.common.room.enums.RoomStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RoomGenerationJobConfig {

    public static final String JOB_NAME = "RoomGenerationJob2";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final RoomRepository roomRepository;
    private final RoomTypeStockRepository stockRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final ActualRoomRepository actualRoomRepository;

    @Bean
    public ItemReader<Room> roomInsertReader() {
        return new ItemReader<>() {
            private Stream<RoomTypeIdAccommodationIdProjection> roomTypeStream;
            private final List<Room> buffer = new ArrayList<>();
            private Map<Long, Long> roomCountMap;
            private boolean initialized = false;

            @Override
            public Room read() {
                if (!initialized) {
                    init();
                }

                if (!buffer.isEmpty()) {
                    return buffer.removeFirst();
                }

                return readNext();
            }

            private void init() {
                roomTypeStream = roomTypeRepository.streamAllIdAndAccommodationId();

                List<Long> accommodationIds = roomTypeStream
                        .map(RoomTypeIdAccommodationIdProjection::getAccommodationId)
                        .distinct()
                        .toList();

                List<RoomTypeCountProjection> roomCounts = actualRoomRepository.countRoomsGroupedByRoomType(accommodationIds);
                roomCountMap = roomCounts.stream()
                        .collect(Collectors.toMap(RoomTypeCountProjection::getRoomTypeId, RoomTypeCountProjection::getCount));

                roomTypeStream.close();
                roomTypeStream = roomTypeRepository.streamAllIdAndAccommodationId();

                initialized = true;
            }

            private Room readNext() {
                if (roomTypeStream == null) {
                    return null;
                }

                RoomTypeIdAccommodationIdProjection next = roomTypeStream.findFirst().orElse(null);

                if (next == null) {
                    roomTypeStream.close();
                    roomTypeStream = null;
                    return null;
                }

                Long roomTypeId = next.getId();
                Long count = roomCountMap.getOrDefault(roomTypeId, 0L);

                YearMonth nextMonth = YearMonth.now().plusMonths(1);
                LocalDate start = nextMonth.atDay(1);
                LocalDate end = nextMonth.atEndOfMonth();

                for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                    for (int i = 0; i < count; i++) {
                        buffer.add(Room.builder()
                                .id(UUID.randomUUID().toString())
                                .roomType(RoomType.builder().id(roomTypeId).build())
                                .date(date)
                                .status(RoomStatus.AVAILABLE)
                                .build());
                    }
                }

                return buffer.isEmpty() ? null : buffer.removeFirst();
            }
        };
    }

    @Bean
    public ItemWriter<Room> roomInsertWriter() {
        return roomRepository::saveAll;
    }

    @Bean
    public Step roomInsertStep() {
        return new StepBuilder("roomInsertStep", jobRepository)
                .<Room, Room>chunk(100, transactionManager)
                .reader(roomInsertReader())
                .writer(roomInsertWriter())
                .build();
    }

    @Bean
    public ItemReader<RoomTypeStock> stockUpdateReader() {
        return new ItemReader<>() {
            private final List<RoomTypeStock> stocks = new ArrayList<>();
            private boolean initialized = false;

            @Override
            public RoomTypeStock read() {
                if (!initialized) {
                    List<RoomType> roomTypes = roomTypeRepository.findAll();
                    YearMonth nextMonth = YearMonth.now().plusMonths(1);
                    LocalDate start = nextMonth.atDay(1);
                    LocalDate end = nextMonth.atEndOfMonth();

                    for (RoomType roomType : roomTypes) {
                        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                            RoomTypeDateId id = new RoomTypeDateId(roomType.getId(), date);
                            if (stockRepository.existsById(id)) continue; // 멱등성 처리

                            int count = roomRepository.countByRoomTypeAndDate(roomType, date);
                            stocks.add(RoomTypeStock.builder()
                                    .id(id)
                                    .stock(count)
                                    .build());
                        }
                    }

                    initialized = true;
                }

                return stocks.isEmpty() ? null : stocks.removeFirst();
            }
        };
    }

    @Bean
    public ItemWriter<RoomTypeStock> stockUpdateWriter() {
        return stockRepository::saveAll;
    }

    @Bean
    public Step stockUpdateStep() {
        return new StepBuilder("stockUpdateStep", jobRepository)
                .<RoomTypeStock, RoomTypeStock>chunk(100, transactionManager)
                .reader(stockUpdateReader())
                .writer(stockUpdateWriter())
                .build();
    }

    @Bean
    public Job roomGenerationJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(roomInsertStep())
                .next(stockUpdateStep())
                .build();
    }
}
