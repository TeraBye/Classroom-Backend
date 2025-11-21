package com.example.question_service.exception;

import com.example.question_service.dto.response.ApiResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(Exception e) {
        return ResponseEntity.internalServerError().body(
                ApiResponse.<String>builder()
                        .code(1001)
                        .message(e.getMessage())
                        .build()
        );
    }

    // Lỗi path variable hoặc query param bị sai kiểu
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ex.getRequiredType();
        String message = String.format("Tham số '%s' phải có kiểu %s",
                ex.getName(), ex.getRequiredType().getSimpleName());

        return ResponseEntity.badRequest().body(
                ApiResponse.<String>builder()
                        .code(4002)
                        .message(message)
                        .build()
        );
    }

    // Lỗi business logic: custom BadRequestException
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<String>> handleBadRequest(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.<String>builder()
                        .code(4003)
                        .message(ex.getMessage())
                        .build()
        );
    }

    // Không có quyền truy cập
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ApiResponse.<String>builder()
                        .code(4030)
                        .message("Bạn không có quyền thực hiện thao tác này")
                        .build()
        );
    }
}
