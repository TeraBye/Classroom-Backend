package com.example.assignment_service.service;

import com.example.assignment_service.dto.request.AssignmentCreateRequest;
import com.example.assignment_service.dto.request.AssignmentSubmitRequest;
import com.example.assignment_service.dto.request.AssignmentUpdateRequest;
import com.example.assignment_service.dto.request.ListIdRequest;
import com.example.assignment_service.dto.response.AssignmentDetailResponse;
import com.example.assignment_service.dto.response.AssignmentResponse;
import com.example.assignment_service.entity.AssignmentDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface AssignmentService {
    AssignmentResponse createAssignment(AssignmentCreateRequest request) throws GeneralSecurityException, IOException;
    List<AssignmentResponse> getAllAssignments();
    AssignmentResponse getAssignmentById(int assignmentId);
    List<AssignmentResponse> getAssignmentsByIds(ListIdRequest request);
    AssignmentResponse updateAssignment(int assignmentId, AssignmentUpdateRequest request) throws GeneralSecurityException, IOException;
    void deleteAssignment(int assignmentId) throws GeneralSecurityException, IOException;
    AssignmentDetailResponse submitAssignment(AssignmentSubmitRequest request) throws IOException, GeneralSecurityException;
    Page<AssignmentDetailResponse> getSubmissionsByAssignment(Integer assignmentId, int page, int size);
    boolean checkSubmission(Integer assignmentId, String studentUsername);
}
