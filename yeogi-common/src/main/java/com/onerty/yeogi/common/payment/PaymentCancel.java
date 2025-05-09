package com.onerty.yeogi.common.payment;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCancel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime canceledAt;
    private String reason;
    private int refundAmount;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
