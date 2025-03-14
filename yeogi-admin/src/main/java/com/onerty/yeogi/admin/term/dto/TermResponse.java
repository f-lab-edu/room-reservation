package com.onerty.yeogi.admin.term.dto;

import com.onerty.yeogi.common.term.Term;

public record TermResponse(
        Long termId,
        String title,
        boolean isRequired,
        String content,
        Integer version
) {

    public static TermResponse from(Term term) {
        return new TermResponse(
                term.getTermId(),
                term.getTitle(),
                term.isRequired(),
                term.getContent(),
                term.getVersion()
        );
    }
}
