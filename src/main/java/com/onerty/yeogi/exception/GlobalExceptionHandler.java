package com.onerty.yeogi.exception;

import com.onerty.yeogi.util.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static com.onerty.yeogi.exception.ErrorType.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(YeogiException.class)
    public ResponseEntity<BaseResponse.error> handleYeogiException(YeogiException ex, WebRequest request) {
        ErrorType errorType = ex.getErrorType();

        BaseResponse.error errorResponse = new BaseResponse.error(
                request.getDescription(false),
                errorType
        );

        return ResponseEntity.status(errorType.getHttpStatus().value()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse.error> handleGenericException(Exception ex, WebRequest request) {
        BaseResponse.error errorResponse = new BaseResponse.error(
                request.getDescription(false),
                INTERNAL_SERVER_ERROR
        );

        return ResponseEntity.status(500).body(errorResponse);
    }
}
