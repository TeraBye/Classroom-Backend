package com.example.logging_service.dto.request;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SearchAuditLogRequest {
    private String username;
}
