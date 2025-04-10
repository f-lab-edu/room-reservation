


package com.onerty.yeogi.common.room;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

}

