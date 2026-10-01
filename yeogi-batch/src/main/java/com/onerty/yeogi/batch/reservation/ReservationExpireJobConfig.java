package com.onerty.yeogi.batch.reservation;

import com.onerty.yeogi.batch.room.repository.RoomTypeStockRepository;
import com.onerty.yeogi.common.exception.ErrorType;
import com.onerty.yeogi.common.exception.YeogiException;
import com.onerty.yeogi.common.room.Reservation;
import com.onerty.yeogi.common.room.Room;
import com.onerty.yeogi.common.room.RoomTypeDateId;
import com.onerty.yeogi.common.room.RoomTypeStock;
import com.onerty.yeogi.common.room.enums.ReservationStatus;
import com.onerty.yeogi.common.room.enums.RoomStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ReservationExpireJobConfig {

    public static final String JOB_NAME = "ReservationExpireJob";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ReservationRepository reservationRepository;
    private final RoomTypeStockRepository stockRepository;

    @Bean
    public Job reservationExpireJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(reservationExpireStep())
                .build();
    }

    @Bean
    public Step reservationExpireStep() {
        return new StepBuilder("reservationExpireStep", jobRepository)
                .<Reservation, Reservation>chunk(100, transactionManager)
                .reader(expiredReservationReader())
                .processor(expiredReservationProcessor())
                .writer(expiredReservationWriter())
                .build();
    }

    @Bean
    public ItemReader<Reservation> expiredReservationReader() {
        return new ItemReader<>() {
            private final List<Reservation> expired = reservationRepository
                    .findExpiredWithRooms(ReservationStatus.PENDING, LocalDateTime.now().minusMinutes(15));
            private final Iterator<Reservation> iterator = expired.iterator();

            {
                log.info(" 만료 예약 조회 수: {}", expired.size());
            }

            @Override
            public Reservation read() {
                return iterator.hasNext() ? iterator.next() : null;
            }
        };
    }

    @Bean
    public ItemProcessor<Reservation, Reservation> expiredReservationProcessor() {
        return reservation -> {
            log.info(" 처리 중인 예약 ID: {}", reservation.getId());

            reservation.setStatus(ReservationStatus.CANCELED);

            if (reservation.getRooms() == null) {
                log.warn("⚠️ 연결된 Room이 없습니다 reservationId={}", reservation.getId());
            } else {
                for (Room room : reservation.getRooms()) {
                    room.setStatus(RoomStatus.AVAILABLE);
                    room.setReservation(null); // 양방향 해제
                }
            }

            return reservation;
        };
    }

    @Bean
    public ItemWriter<Reservation> expiredReservationWriter() {
        return reservations -> {
            for (Reservation reservation : reservations) {
                try {
                    LocalDate start = reservation.getCheckIn();
                    LocalDate end = reservation.getCheckOut();
                    Long roomTypeId = reservation.getRoomType().getId();

                    for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
                        RoomTypeDateId dateId = new RoomTypeDateId(roomTypeId, date);
                        RoomTypeStock stock = stockRepository.findById(dateId)
                                .orElseThrow(() -> new YeogiException(ErrorType.STOCK_NOT_FOUND));
                        stock.setStock(stock.getStock() + 1);
                    }

                    reservationRepository.save(reservation);

                    log.info(" 만료된 예약 자동 취소: id = {}", reservation.getId());

                } catch (Exception e) {
                    log.error("⚠️ Writer 처리 중 예외 발생: reservationId = {}, 에러: {}", reservation.getId(), e.getMessage(), e);
                }
            }
        };
    }

}
