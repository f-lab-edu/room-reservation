package com.onerty.yeogi.common.reservation;

import com.onerty.yeogi.common.payment.Payment;
import com.onerty.yeogi.common.room.Room;
import com.onerty.yeogi.common.room.RoomType;
import com.onerty.yeogi.common.room.enums.ReservationStatus;
import com.onerty.yeogi.common.user.User;
import com.onerty.yeogi.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate checkIn;
    private LocalDate checkOut;
    private int guestCount;
    private int totalPrice;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Payment payment;

    public void addRoom(Room room) {
        rooms.add(room);
        room.setReservation(this);
    }

    @Version
    private Long version;

}
