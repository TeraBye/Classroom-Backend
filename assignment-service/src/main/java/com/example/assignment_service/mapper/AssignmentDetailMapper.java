package com.example.assignment_service.mapper;

import com.example.assignment_service.dto.request.AssignmentSubmitRequest;
import com.example.assignment_service.dto.response.AssignmentDetailResponse;
import com.example.assignment_service.entity.Assignment;
import com.example.assignment_service.entity.AssignmentDetail;
import com.example.assignment_service.enums.AssignmentDetailStatus;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AssignmentDetailMapper {
    AssignmentDetail toAssignmentDetail(AssignmentSubmitRequest request);

    @Mapping(target = "assignmentId", source = "assignment.id")
    @Mapping(target = "status", expression = "java(mapStatus(assignmentDetail.getSubmitTime(), deadline))")
    @Mapping(target = "submissionCount", source = "submissionCount")
    AssignmentDetailResponse toAssignmentDetailResponse(AssignmentDetail assignmentDetail, @Context LocalDateTime deadline);

    List<AssignmentDetailResponse> toAssignmentDetailResponses(List<Assignment> assignments);

    default AssignmentDetailStatus mapStatus(LocalDateTime submitTime, @Context LocalDateTime deadline) {
        if (submitTime == null || deadline == null) return null;
        return submitTime.isAfter(deadline) ? AssignmentDetailStatus.LATE : AssignmentDetailStatus.ON_TIME;
    }
}