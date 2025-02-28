package com.onerty.yeogi.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Nickname {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NicknameType type;

    private String value;

    public Nickname(NicknameType type, String value) {
        this.type = type;
        this.value = value;
    }
}
