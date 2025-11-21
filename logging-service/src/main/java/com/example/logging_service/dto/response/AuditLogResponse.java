package com.example.logging_service.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuditLogResponse {
    private Long id;

    private String username;

    private String role;

    private String action;

    private String description;

    private LocalDateTime createdAt;
}
