package com.onerty.yeogi.batch.reservation;

import com.onerty.yeogi.common.reservation.Reservation;
import com.onerty.yeogi.common.room.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByStatusAndUpdatedAtBefore(ReservationStatus status, LocalDateTime before);

    @Query("""
    SELECT r FROM Reservation r
    LEFT JOIN FETCH r.rooms
    WHERE r.status = :status
    AND r.updatedAt < :before
    """)
    List<Reservation> findExpiredWithRooms(
            @Param("status") ReservationStatus status,
            @Param("before") LocalDateTime before
    );

}
