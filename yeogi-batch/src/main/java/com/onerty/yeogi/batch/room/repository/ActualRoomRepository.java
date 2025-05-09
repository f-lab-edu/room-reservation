package com.onerty.yeogi.batch.room.repository;

import com.onerty.yeogi.batch.room.repository.projections.ActualRoomProjection;
import com.onerty.yeogi.common.room.ActualRoom;
import com.onerty.yeogi.common.room.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public interface ActualRoomRepository extends JpaRepository<ActualRoom, Long> {

    @Query("SELECT ar.roomType.id AS roomTypeId, COUNT(ar) AS count " +
            "FROM ActualRoom ar " +
            "WHERE ar.roomType.accommodation.id IN :accommodationIds " +
            "GROUP BY ar.roomType.id")
    List<RoomTypeCountProjection> countRoomsGroupedByRoomType(@Param("accommodationIds") List<Long> accommodationIds);


    @Query("""
        select 
            a.id as id, 
            a.roomNumber as roomNumber, 
            a.floor as floor
        from ActualRoom a
    """)
    Stream<ActualRoomProjection> streamAllActualRoom();

}
