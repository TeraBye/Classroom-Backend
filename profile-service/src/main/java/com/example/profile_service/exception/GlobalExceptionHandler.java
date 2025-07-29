package com.example.profile_service.exception;

import com.example.profile_service.dto.response.ApiResponse;
import org.apache.http.auth.AuthenticationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Lỗi validation (DTO - @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(
                ApiResponse.<String>builder()
                        .code(4001)
                        .message(message)
                        .build()
        );
    }

    // Lỗi path variable hoặc query param bị sai kiểu
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("Tham số '%s' phải có kiểu %s",
                ex.getName(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "đúng");

        return ResponseEntity.badRequest().body(
                ApiResponse.<String>builder()
                        .code(4002)
                        .message(message)
                        .build()
        );
    }

    // Lỗi dữ liệu không tìm thấy (custom exception)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.<String>builder()
                        .code(4040)
                        .message(ex.getMessage())
                        .build()
        );
    }

    // Lỗi business logic: custom BadRequestException
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<String>> handleBadRequest(BusinessException ex) {
        return ResponseEntity.badRequest().body(
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

    // Chưa đăng nhập
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<String>> handleAuthentication(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponse.<String>builder()
                        .code(4010)
                        .message("Bạn cần đăng nhập để tiếp tục")
                        .build()
        );
    }

    // Catch-all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleUnknown(Exception ex) {
        ex.printStackTrace(); // hoặc log
        return ResponseEntity.internalServerError().body(
                ApiResponse.<String>builder()
                        .code(5000)
                        .message(ex.getMessage())
                        .build()
        );
    }
}
