package com.onerty.yeogi.common.room;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RoomTypeDateId implements Serializable {
    private Long roomTypeId;
    private LocalDate date;
}

