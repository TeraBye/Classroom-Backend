package org.example.scoreservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
    private int code = 1000;
    private String message;
    private T result;
}
