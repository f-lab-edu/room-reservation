package com.onerty.yeogi.customer.reservation;

import com.onerty.yeogi.common.exception.ErrorType;
import com.onerty.yeogi.common.exception.YeogiException;
import com.onerty.yeogi.common.reservation.TempReservation;
import com.onerty.yeogi.common.room.RoomTypeDateId;
import com.onerty.yeogi.common.room.RoomTypeStock;
import com.onerty.yeogi.customer.room.RoomTypeStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TempReservationExpirationHandler implements MessageListener {

    private final TempReservationRepository tempReservationRepository;
    private final RoomTypeStockRepository stockRepository;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        log.info("🧨 Redis TTL expired key = {}", expiredKey);

        if (!expiredKey.startsWith("reserve:temp:")) return;

        try {
            Long tempReservationId = Long.parseLong(expiredKey.replace("reserve:temp:", ""));
            Optional<TempReservation> optional = tempReservationRepository.findById(tempReservationId);
            if (optional.isEmpty()) return;

            TempReservation temp = optional.get();

            // 날짜별 재고 복구
            for (LocalDate date = temp.getCheckIn(); date.isBefore(temp.getCheckOut()); date = date.plusDays(1)) {
                RoomTypeDateId dateId = new RoomTypeDateId(temp.getRoomTypeId(), date);
                RoomTypeStock stock = stockRepository.findById(dateId)
                        .orElseThrow(() -> new YeogiException(ErrorType.STOCK_NOT_FOUND));

                stock.setStock(stock.getStock() + 1);
            }

            tempReservationRepository.deleteById(tempReservationId);
            log.info("🧹 TempReservation #{} expired and cleaned up", tempReservationId);

        } catch (Exception e) {
            log.error("TTL 만료 처리 중 예외 발생: {}", e.getMessage(), e);
        }
    }
}
