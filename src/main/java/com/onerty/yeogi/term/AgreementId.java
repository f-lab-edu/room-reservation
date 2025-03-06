package com.onerty.yeogi.term;

import com.onerty.yeogi.user.User;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AgreementId {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String title;
    private Integer version;
}
