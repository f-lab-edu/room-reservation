package com.onerty.yeogi.batch.room.repository;


import com.onerty.yeogi.common.room.RoomTypeDateId;
import com.onerty.yeogi.common.room.RoomTypeStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomTypeStockRepository extends JpaRepository<RoomTypeStock, RoomTypeDateId> {
}
