package org.example.stablecoinchecker.config;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.stablecoinchecker.config.ErrorResponse.FieldErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> Exception(Exception e) {
        final ErrorResponse errorResponse = ErrorResponse.create(
                "예기치 않은 예외가 발생 했습니다."
        );

        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> BindException(BindException e) {
        List<FieldErrorResponse> errors = new ArrayList<>();

        List<FieldError> fieldErrors = e.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errors.add(new FieldErrorResponse(
                    fieldError.getField(),
                    fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue().toString(),
                    fieldError.getDefaultMessage()
            ));
        }

        final ErrorResponse errorResponse = ErrorResponse.create(
                "쿼리 파라미터를 확인해 주세요",
                errors
        );

        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({IllegalArgumentException.class, RuntimeException.class})
    protected ResponseEntity<ErrorResponse> IllegalArgumentException(RuntimeException e) {
        final ErrorResponse errorResponse = ErrorResponse.create(
                e.getMessage()
        );

        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Object> HttpRequestMethodNotSupportedException(
            final HttpRequestMethodNotSupportedException e
    ) {
        final ErrorResponse errorResponse = ErrorResponse.create(
                "HTTP 메서드를 확인해 주세요"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            final MissingServletRequestParameterException e
    ) {
        final ErrorResponse errorResponse = ErrorResponse.create(
                "요청 파라미터를 확인해 주세요"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
