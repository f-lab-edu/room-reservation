package com.onerty.yeogi.common.room;

import jakarta.persistence.*;
import lombok.*;
import com.onerty.yeogi.common.user.Host;


import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;

    @ManyToOne
    @JoinColumn(name = "host_id")
    private Host host;

    @OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL)
    private List<RoomType> roomTypes;
}

