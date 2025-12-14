package com.example.logging_service.service;

import com.example.logging_service.dto.request.SearchAuditLogRequest;
import com.example.logging_service.dto.response.AuditLogResponse;
import com.example.logging_service.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLogService {
    Page<AuditLogResponse> getAuditLogs(SearchAuditLogRequest request, Pageable pageable);
}
