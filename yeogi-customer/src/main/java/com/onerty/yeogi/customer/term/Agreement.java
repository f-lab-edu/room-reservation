package com.onerty.yeogi.customer.term;

import com.onerty.yeogi.customer.util.BaseEntity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Agreement extends BaseEntity {

    @EmbeddedId
    private AgreementId agreementId;

    private boolean isAgreed;

}
