package com.onerty.yeogi.admin.term.dto;

import com.onerty.yeogi.common.exception.ErrorType;
import com.onerty.yeogi.common.exception.YeogiException;
import com.onerty.yeogi.common.term.TermTitle;
import com.onerty.yeogi.common.util.Checkable;
import io.micrometer.common.util.StringUtils;

public record UpdateTermRequest(
        String title,
        String content
) implements Checkable {

    @Override
    public void check() {

        if (StringUtils.isBlank(title) || StringUtils.isBlank(content)) {
            throw new YeogiException(ErrorType.TERM_MISSING_FIELD);
        }

        if (TermTitle.notExists(title)) {
            throw new YeogiException(ErrorType.INVALID_TERM_TITLE);
        }
    }
}

