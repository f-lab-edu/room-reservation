package com.onerty.yeogi.common.payment;

import com.onerty.yeogi.common.reservation.Reservation;
import com.onerty.yeogi.common.room.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime paidAt;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
    private Long tempReservationId;


}
