package com.onerty.yeogi.admin.term;

import com.onerty.yeogi.common.term.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {

    boolean existsByTitle(String title);

    Optional<Term> findTopByTitleOrderByVersionDesc(String title);

}
