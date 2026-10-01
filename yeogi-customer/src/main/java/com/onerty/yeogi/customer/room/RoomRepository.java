package com.onerty.yeogi.customer.room;

import com.onerty.yeogi.common.room.Room;
import com.onerty.yeogi.common.room.RoomType;
import com.onerty.yeogi.common.room.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

        Optional<Room> findFirstByRoomTypeAndDateAndStatus(RoomType roomType, LocalDate date, RoomStatus status);

}
