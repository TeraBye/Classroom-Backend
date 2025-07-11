package com.example.classroom_service.mapper;

import com.example.classroom_service.dto.request.SubjectCreateRequest;
import com.example.classroom_service.dto.request.SubjectUpdateRequest;
import com.example.classroom_service.dto.response.SubjectResponse;
import com.example.classroom_service.entity.Subject;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubjectMapper {
    Subject toSubject(SubjectCreateRequest request);

    SubjectResponse toSubjectResponse(Subject subject);
    List<SubjectResponse> toSubjectResponses(List<Subject> subjects);
    void updateSubject(@MappingTarget Subject subject, SubjectUpdateRequest request);
}