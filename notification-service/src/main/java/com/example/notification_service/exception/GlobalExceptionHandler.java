package com.example.notification_service.exception;

import com.example.notification_service.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(Exception e) {
        return ResponseEntity.internalServerError().body(
                ApiResponse.builder()
                        .code(1001)
                        .message(e.getMessage())
                        .build()
        );
    }
}
