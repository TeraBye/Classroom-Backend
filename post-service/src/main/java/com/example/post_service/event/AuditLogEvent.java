package com.example.post_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogEvent {
    private String username;
    private String role;
    private String action;
    private String description;
}
