package com.example.import_service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ImportJobResponse {
    private Long id;
    private String type;
    private String status;
    private String message;
    private String username;
    private String fileName;
    private Instant createdAt;
    private Instant updatedAt;
}
