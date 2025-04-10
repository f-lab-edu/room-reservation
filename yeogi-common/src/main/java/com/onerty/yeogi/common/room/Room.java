package com.onerty.yeogi.common.room;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomNumber;
    private String floor;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    public enum RoomStatus {
        AVAILABLE, UNDER_MAINTENANCE
    }

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

    private LocalDate date;

}