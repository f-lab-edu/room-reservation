package com.onerty.yeogi.customer.reservation;

import com.onerty.yeogi.common.reservation.TempReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempReservationRepository extends JpaRepository<TempReservation, Long> {
}
