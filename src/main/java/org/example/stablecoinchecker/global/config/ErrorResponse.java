package org.example.stablecoinchecker.global.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record ErrorResponse(
        LocalDateTime timeStamp,
        String message,
        List<FieldErrorResponse> errorList
) {
    static ErrorResponse create(String message) {
        return new ErrorResponse(LocalDateTime.now(), message, new ArrayList<>());
    }

    static ErrorResponse create(String message, List<FieldErrorResponse> errorList) {
        return new ErrorResponse(LocalDateTime.now(), message, errorList);
    }

    public record FieldErrorResponse(String field, String value, String message) {
    }
}
