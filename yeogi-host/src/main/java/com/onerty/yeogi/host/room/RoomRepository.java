package com.onerty.yeogi.host.room;

import com.onerty.yeogi.common.room.Room;
import com.onerty.yeogi.common.room.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface RoomRepository extends JpaRepository<Room, Long> {

    int countByRoomTypeAndDate(RoomType roomType, LocalDate date);

    boolean existsByRoomTypeAndDate(RoomType roomType, LocalDate date);
}
