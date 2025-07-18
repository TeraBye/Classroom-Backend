package com.example.assignment_service.service;

import com.example.assignment_service.dto.request.AssignmentCreateRequest;
import com.example.assignment_service.dto.request.AssignmentSubmitRequest;
import com.example.assignment_service.dto.request.AssignmentUpdateRequest;
import com.example.assignment_service.dto.response.AssignmentDetailResponse;
import com.example.assignment_service.dto.response.AssignmentResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface AssignmentService {
    AssignmentResponse createAssignment(AssignmentCreateRequest request);
    List<AssignmentResponse> getAllAssignments();
    AssignmentResponse getAssignmentById(int assignmentId);
    AssignmentResponse updateAssignment(int assignmentId, AssignmentUpdateRequest request);
    void deleteAssignment(int assignmentId);
    AssignmentDetailResponse submitAssignment(AssignmentSubmitRequest request) throws IOException, GeneralSecurityException;
}
