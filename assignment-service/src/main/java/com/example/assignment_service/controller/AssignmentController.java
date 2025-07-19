package com.example.assignment_service.controller;

import com.example.assignment_service.dto.request.AssignmentCreateRequest;
import com.example.assignment_service.dto.request.AssignmentSubmitRequest;
import com.example.assignment_service.dto.request.AssignmentUpdateRequest;
import com.example.assignment_service.dto.response.ApiResponse;
import com.example.assignment_service.dto.response.AssignmentDetailResponse;
import com.example.assignment_service.dto.response.AssignmentResponse;
import com.example.assignment_service.service.AssignmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Assignment Controller")
public class AssignmentController {
    AssignmentService assignmentService;

    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ApiResponse<AssignmentResponse> createAssignment(@Valid @ModelAttribute AssignmentCreateRequest request) throws GeneralSecurityException, IOException {
        return ApiResponse.<AssignmentResponse>builder()
                .result(assignmentService.createAssignment(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<AssignmentResponse>> getAllAssignments() {
        return ApiResponse.<List<AssignmentResponse>>builder()
                .result(assignmentService.getAllAssignments())
                .build();
    }

    @GetMapping("/{assignmentId}")
    public ApiResponse<AssignmentResponse> getAssignmentById(@PathVariable int assignmentId) {
        return ApiResponse.<AssignmentResponse>builder()
                .result(assignmentService.getAssignmentById(assignmentId))
                .build();
    }

    @PutMapping("/{assignmentId}")
    public ApiResponse<AssignmentResponse> updateAssignment(@PathVariable int assignmentId, @RequestBody AssignmentUpdateRequest request) {
        return ApiResponse.<AssignmentResponse>builder()
                .result(assignmentService.updateAssignment(assignmentId, request))
                .build();
    }

    @DeleteMapping("/{assignmentId}")
    public ApiResponse<Void> deleteAssignment(@PathVariable int assignmentId) {
        assignmentService.deleteAssignment(assignmentId);
        return ApiResponse.<Void>builder()
                .message("Delete assignment successfully")
                .build();
    }

    @PostMapping(value = "/submit", consumes = {"multipart/form-data"})
    public ApiResponse<AssignmentDetailResponse> submitAssignment(
//            @ModelAttribute được sử dụng khi gửi dữ liệu dạng multipart/form-data
            @Valid @ModelAttribute AssignmentSubmitRequest request) throws GeneralSecurityException, IOException {
        return ApiResponse.<AssignmentDetailResponse>builder()
                .result(assignmentService.submitAssignment(request))
                .build();
    }
}
