package com.onerty.yeogi.exception;

public class YeogiException extends RuntimeException {
    ErrorType errorType;

    public YeogiException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
