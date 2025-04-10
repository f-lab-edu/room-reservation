package com.onerty.yeogi.common.room;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;
    private int capacity;
    private int pricePerNight;
    private String description;

    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL)
    private List<Room> rooms;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "roomTypeId", referencedColumnName = "id")
    private List<RoomTypeStock> stocks;

}

