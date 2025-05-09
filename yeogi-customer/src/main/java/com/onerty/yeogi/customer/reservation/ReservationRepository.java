package com.onerty.yeogi.customer.reservation;

import com.onerty.yeogi.common.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
