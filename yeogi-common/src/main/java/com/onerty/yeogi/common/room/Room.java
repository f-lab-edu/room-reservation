package com.onerty.yeogi.common.room;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Builder
@Setter
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

    public enum RoomStatus {
        AVAILABLE, UNDER_MAINTENANCE
    }

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

    private LocalDate date;

}