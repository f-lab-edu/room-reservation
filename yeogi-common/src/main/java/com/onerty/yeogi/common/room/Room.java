package com.onerty.yeogi.common.room;

import com.onerty.yeogi.common.reservation.Reservation;
import com.onerty.yeogi.common.room.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    @Id
    @Column(name = "id")
    private String id;

    private String roomNumber;
    private String floor;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

}