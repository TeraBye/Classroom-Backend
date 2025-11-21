package com.example.identity_service.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION("uncategorized exception", 9999, HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED("User existed", 1002, HttpStatus.BAD_REQUEST),
    INVALID_KEY("Invalid key", 1001, HttpStatus.BAD_REQUEST),
    INVALID_USERNAME("Username must be at least {min} characters", 1003, HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD("Password must be at least {min} characters", 1004, HttpStatus.BAD_REQUEST),
    USER_NOT_EXIST("user not existed", 1005, HttpStatus.NOT_FOUND),
    UNAUTHENTICATED("unauthenticated", 1006, HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("You do not have permission", 1007, HttpStatus.FORBIDDEN),
    INVALID_DOB("Your age must at least {min}", 1008, HttpStatus.BAD_REQUEST),
    INVALID_TOKEN("Invalid token", 1009, HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED("Token expired", 1010, HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD("Wrong password", 1011, HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final int code;
    private final HttpStatus statusCode;
}
