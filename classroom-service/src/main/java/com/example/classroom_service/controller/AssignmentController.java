package com.example.classroom_service.controller;

import com.example.assignment_service.dto.request.AssignmentCreateRequest;
import com.example.assignment_service.dto.request.AssignmentUpdateRequest;
import com.example.assignment_service.dto.response.ApiResponse;
import com.example.assignment_service.dto.response.AssignmentResponse;
import com.example.assignment_service.service.AssignmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Assignment Controller")
public class AssignmentController {
    AssignmentService assignmentService;
    @PostMapping("/create")
    public ApiResponse<AssignmentResponse> createAssignment(@RequestBody AssignmentCreateRequest request) {
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
}
