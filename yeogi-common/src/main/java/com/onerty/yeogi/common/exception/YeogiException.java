package com.onerty.yeogi.common.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Getter
public class YeogiException extends RuntimeException {

    ErrorType errorType;
    Map<String, Object> parameters;
    Consumer<String> logConsumer;

    public YeogiException(ErrorType errorType) {
        this.errorType = errorType;
        this.logConsumer = log::warn;
    }

    public YeogiException(ErrorType errorType, Consumer<String> logConsumer) {
        this.errorType = errorType;
        this.logConsumer = logConsumer;
    }

    public YeogiException(ErrorType errorType, Map<String, Object> parameters, Consumer<String> logConsumer) {
        this.errorType = errorType;
        this.parameters = parameters;
        this.logConsumer = logConsumer;
    }
}
