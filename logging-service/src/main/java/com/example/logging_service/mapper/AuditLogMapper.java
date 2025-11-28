package com.example.logging_service.mapper;

import com.example.logging_service.dto.response.AuditLogResponse;
import com.example.logging_service.entity.AuditLog;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuditLogMapper {
    AuditLogResponse toResponse(AuditLog auditLog);
}