package com.onerty.yeogi.customer.term;

import com.onerty.yeogi.common.term.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {

    @Query("SELECT t FROM Term t WHERE t.version = (SELECT MAX(t2.version) FROM Term t2 WHERE t2.title = t.title)")
    List<Term> findTermsWithLatestTermDetail();

}