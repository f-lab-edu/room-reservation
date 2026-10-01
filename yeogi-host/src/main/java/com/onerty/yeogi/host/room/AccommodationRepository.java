package com.onerty.yeogi.host.room;

import com.onerty.yeogi.common.room.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
}
