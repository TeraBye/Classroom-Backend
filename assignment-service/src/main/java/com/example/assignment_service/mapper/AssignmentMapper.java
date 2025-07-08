package com.example.assignment_service.mapper;

import com.example.assignment_service.dto.request.AssignmentCreateRequest;
import com.example.assignment_service.dto.request.AssignmentUpdateRequest;
import com.example.assignment_service.dto.response.AssignmentResponse;
import com.example.assignment_service.entity.Assignment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssignmentMapper {
    Assignment toAssignment(AssignmentCreateRequest request);
    AssignmentResponse toAssignmentResponse(Assignment assignment);
    List<AssignmentResponse> toAssignmentResponses(List<Assignment> assignments);
    void updateAssignment(@MappingTarget Assignment assignment, AssignmentUpdateRequest request);
}
