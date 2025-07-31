package com.example.question_service.exception;

import com.example.question_service.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
