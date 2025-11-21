package com.example.import_service.mapper;

import com.example.import_service.dto.response.ImportJobResponse;
import com.example.import_service.entity.ImportJob;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ImportJobMapper {
    ImportJobResponse toJobResponse(ImportJob importJob);
}