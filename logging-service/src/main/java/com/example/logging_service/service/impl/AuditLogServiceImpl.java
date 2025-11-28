package com.example.logging_service.service.impl;

import com.example.logging_service.dto.response.AuditLogResponse;
import com.example.logging_service.entity.AuditLog;
import com.example.logging_service.mapper.AuditLogMapper;
import com.example.logging_service.repository.AuditLogRepository;
import com.example.logging_service.service.AuditLogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuditLogServiceImpl implements AuditLogService {
    AuditLogRepository auditLogRepository;
    AuditLogMapper logMapper;

    @Override
    public Page<AuditLogResponse> getAuditLogs(Pageable pageable) {
        return auditLogRepository.findAll(pageable).map(logMapper::toResponse);
    }
}
