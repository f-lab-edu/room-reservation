package com.onerty.yeogi.term;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {
    @Query("""
            SELECT t
            FROM Term t
            JOIN FETCH t.termDetails td
            WHERE td.version = (
                SELECT MAX(tdSub.version)
                FROM TermDetail tdSub
                WHERE tdSub.term = t
            )
            """)
    List<Term> findTermsWithLatestTermDetail();

}