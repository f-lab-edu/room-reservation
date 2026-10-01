package com.onerty.yeogi.batch.room.repository;


import com.onerty.yeogi.batch.room.repository.projections.RoomTypeIdAccommodationIdProjection;
import com.onerty.yeogi.common.room.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.stream.Stream;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

    @Query("""
    SELECT r.id AS id, r.accommodation.id AS accommodationId
    FROM RoomType r
    """)
    Stream<RoomTypeIdAccommodationIdProjection> streamAllIdAndAccommodationId();

}
