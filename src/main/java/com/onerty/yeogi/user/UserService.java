package com.onerty.yeogi.user;

import com.onerty.yeogi.term.Term;
import com.onerty.yeogi.term.TermRepository;
import com.onerty.yeogi.term.dto.TermDto;
import com.onerty.yeogi.term.dto.TermResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final TermRepository termRepository;

    public TermResponse getTerms() {
        List<Term> terms = termRepository.findTermsWithLatestTermDetail();

        List<TermDto> termDtos = terms.stream()
                .map(term -> new TermDto(
                        term.getTermId(),
                        term.getTitle(),
                        term.getTermDetails().isEmpty() ? "" : term.getTermDetails().get(0).getContent(),
                        term.isRequired()
                ))
                .collect(Collectors.toList());

        return new TermResponse(termDtos);
    }

}
