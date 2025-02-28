package com.onerty.yeogi.term;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {

    @Query("""
            SELECT a.isAgreed
            FROM Agreement a
            WHERE a.agreementId.userId = :userId
              AND a.agreementId.termId = :termId
            """)
    Optional<Boolean> findIsAgreedByAgreementId(Long userId, Long termId);
}
