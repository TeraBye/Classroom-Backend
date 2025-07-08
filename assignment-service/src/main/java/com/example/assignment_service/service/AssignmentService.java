package com.example.assignment_service.service;

import com.example.assignment_service.dto.request.AssignmentCreateRequest;
import com.example.assignment_service.dto.request.AssignmentUpdateRequest;
import com.example.assignment_service.dto.response.AssignmentResponse;

import java.util.List;

public interface AssignmentService {
    AssignmentResponse createAssignment(AssignmentCreateRequest request);
    List<AssignmentResponse> getAllAssignments();
    AssignmentResponse getAssignmentById(int assignmentId);
    AssignmentResponse updateAssignment(int assignmentId, AssignmentUpdateRequest request);
    void deleteAssignment(int assignmentId);
}
