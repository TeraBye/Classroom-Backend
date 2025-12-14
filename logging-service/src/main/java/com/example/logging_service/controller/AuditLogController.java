package com.example.logging_service.controller;

import com.example.logging_service.dto.request.SearchAuditLogRequest;
import com.example.logging_service.dto.response.ApiResponse;
import com.example.logging_service.dto.response.AuditLogResponse;
import com.example.logging_service.service.AuditLogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuditLogController {

    AuditLogService auditLogService;

    @GetMapping
    public ApiResponse<Page<AuditLogResponse>> getAuditLogs(SearchAuditLogRequest request, Pageable pageable) {
        return ApiResponse.<Page<AuditLogResponse>>builder()
                .result(auditLogService.getAuditLogs(request, pageable))
                .build();
    }
}
