package com.onerty.yeogi.common.util;

import com.onerty.yeogi.common.exception.ErrorType;
import lombok.Getter;

@Getter
public class BaseResponse<T> {
    private final T data;
    private final Meta meta;

    public BaseResponse(T data, Meta meta) {
        this.data = data;
        this.meta = meta;
    }

    public record Meta(String message, String code) {
    }

    public static class success<T> extends BaseResponse<T> {
        public success(T data) {
            super(data, new Meta(null, null));
        }
    }

    @Getter
    public static class error {
        private final String url;
        private final int statusCode;
        private final String statusMessage;
        private final String message;
        private final String stack;
        private final Meta meta;

        public error(String url, ErrorType errorType) {
            this.url = url;
            this.statusCode = errorType.getHttpStatus().value();
            this.statusMessage = errorType.getHttpStatus().name();
            this.message = errorType.getMessage();
            this.stack = "";
            this.meta = new Meta(message, errorType.getCode());
        }
    }
}
