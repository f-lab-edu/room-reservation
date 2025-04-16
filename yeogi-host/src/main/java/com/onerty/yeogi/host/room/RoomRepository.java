package com.onerty.yeogi.host.room;

import com.onerty.yeogi.common.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
