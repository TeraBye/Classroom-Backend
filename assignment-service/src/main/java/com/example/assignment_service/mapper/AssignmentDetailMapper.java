package com.example.assignment_service.mapper;

import com.example.assignment_service.dto.request.AssignmentSubmitRequest;
import com.example.assignment_service.dto.response.AssignmentDetailResponse;
import com.example.assignment_service.entity.Assignment;
import com.example.assignment_service.entity.AssignmentDetail;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AssignmentDetailMapper {
    AssignmentDetail toAssignmentDetail(AssignmentSubmitRequest request);

    AssignmentDetailResponse toAssignmentResponse(AssignmentDetail assignmentDetail);

    List<AssignmentDetailResponse> toAssignmentDetailResponses(List<Assignment> assignments);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updateAssignmentDetail(AssignmentDetailResponse assignmentDetailResponse, @MappingTarget AssignmentDetail assignmentDetail);
}