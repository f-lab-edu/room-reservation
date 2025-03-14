package com.onerty.yeogi.admin.term;

import com.onerty.yeogi.admin.term.dto.RegisterTermRequest;
import com.onerty.yeogi.admin.term.dto.TermResponse;
import com.onerty.yeogi.admin.term.dto.UpdateTermRequest;
import com.onerty.yeogi.common.exception.ErrorType;
import com.onerty.yeogi.common.exception.YeogiException;
import com.onerty.yeogi.common.term.Term;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;

    public TermResponse registerTerm(RegisterTermRequest createTermRequest) {

        if (termRepository.existsByTitle(createTermRequest.title())) {
            throw new YeogiException(ErrorType.ALREADY_EXIST_TERM);
        }

        Term term = Term.builder()
                .title(createTermRequest.title())
                .isRequired(createTermRequest.isRequired())
                .content(createTermRequest.content())
                .version(1)
                .build();

        return TermResponse.from(termRepository.save(term));
    }

    public TermResponse updateTermContent(UpdateTermRequest updateTermRequest) {

        Term latestTerm = termRepository.findTopByTitleOrderByVersionDesc(updateTermRequest.title())
                .orElseThrow(() -> new YeogiException(ErrorType.TERM_NOT_FOUND));

        Term newTermVersion = Term.builder()
                .title(latestTerm.getTitle())
                .isRequired(latestTerm.isRequired())
                .content(updateTermRequest.content())
                .version(latestTerm.getVersion() + 1)
                .build();

        return TermResponse.from(termRepository.save(newTermVersion));
    }
}