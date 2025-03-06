package com.onerty.yeogi.term;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AgreementId {
    private Long userId;
    private Long termId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AgreementId that = (AgreementId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(termId, that.termId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, termId);
    }
}
