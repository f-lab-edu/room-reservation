


package com.onerty.yeogi.common.room;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomTypeStock {

    @EmbeddedId
    private RoomTypeDateId id;

    private int stock;

}

