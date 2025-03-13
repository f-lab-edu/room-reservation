package com.onerty.yeogi.customer.exception;

import com.onerty.yeogi.customer.util.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.onerty.yeogi.customer.exception.ErrorType.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(YeogiException.class)
    public ResponseEntity<BaseResponse.error> handleYeogiException(YeogiException ex, WebRequest request) {

        ErrorType errorType = ex.getErrorType();
        Map<String, Object> parameters = ex.getParameters() != null ? ex.getParameters() : new HashMap<>();
        Consumer<String> logger = ex.getLogConsumer();

        String errorMessage = String.format("[YeogiException] Type: %s | Message: %s | Parameters: %s\nStackTrace:\n%s",
                errorType, ex.getMessage(), parameters, getStackTraceAsString(ex));

        logger.accept(errorMessage);

        BaseResponse.error errorResponse = new BaseResponse.error(
                request.getDescription(false),
                errorType
        );

        return ResponseEntity.status(errorType.getHttpStatus().value()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse.error> handleGenericException(Exception ex, WebRequest request) {

        Map<String, Object> parameters = new HashMap<>();

        String errorMessage = String.format("[Unhandled Exception] Path: %s | Message: %s\nStackTrace:\n%s",
                request.getDescription(false), ex.getMessage(), getStackTraceAsString(ex));

        log.error(errorMessage);

        BaseResponse.error errorResponse = new BaseResponse.error(
                request.getDescription(false),
                INTERNAL_SERVER_ERROR
        );

        return ResponseEntity.status(500).body(errorResponse);
    }

    private String getStackTraceAsString(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append("\tat ").append(element).append("\n");
        }
        return sb.toString();
    }
}
