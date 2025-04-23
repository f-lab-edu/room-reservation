package com.onerty.yeogi.batch.room.repository;

import com.onerty.yeogi.common.room.ActualRoom;
import com.onerty.yeogi.common.room.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActualRoomRepository extends JpaRepository<ActualRoom, Long> {

    @Query("SELECT ar.roomType.id AS roomTypeId, COUNT(ar) AS count " +
            "FROM ActualRoom ar " +
            "WHERE ar.roomType.accommodation.id IN :accommodationIds " +
            "GROUP BY ar.roomType.id")
    List<RoomTypeCountProjection> countRoomsGroupedByRoomType(@Param("accommodationIds") List<Long> accommodationIds);

    int countByRoomType(RoomType roomType);

}
