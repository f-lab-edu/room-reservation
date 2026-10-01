package com.onerty.yeogi.customer.term;

import com.onerty.yeogi.common.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {

    @Query("""
            SELECT a.isAgreed
            FROM Agreement a
            WHERE a.agreementId.user = :user
              AND a.agreementId.title = :title
            """)
    Optional<Boolean> findIsAgreedByUserAndTitle(User user, String title);

}
