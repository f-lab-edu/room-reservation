package com.onerty.yeogi.batch.room.repository;

import com.onerty.yeogi.common.room.ActualRoom;
import com.onerty.yeogi.common.room.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActualRoomRepository extends JpaRepository<ActualRoom, Long> {

    List<ActualRoom> findByRoomType(RoomType roomType);

}
