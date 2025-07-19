package com.example.classroom_service.mapper;

import com.example.classroom_service.dto.response.StudentResponse;
import com.example.classroom_service.entity.ClassroomDetail;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClassroomDetailMapper {
    StudentResponse toStudentResponse(ClassroomDetail classroomDetail);
}