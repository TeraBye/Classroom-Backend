package com.example.classroom_service.controller;

import com.example.classroom_service.dto.request.SubjectCreateRequest;
import com.example.classroom_service.dto.request.SubjectUpdateRequest;
import com.example.classroom_service.dto.response.ApiResponse;
import com.example.classroom_service.dto.response.SubjectResponse;
import com.example.classroom_service.service.SubjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/subject")
@Tag(name = "Subject Controller")
public class SubjectController {
    SubjectService subjectService;

    @PostMapping("/create")
    public ApiResponse<SubjectResponse> createSubject(@RequestBody SubjectCreateRequest request) {
        return ApiResponse.<SubjectResponse>builder()
                .result(subjectService.createSubject(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<SubjectResponse>> getAllSubjects() {
        return ApiResponse.<List<SubjectResponse>>builder()
                .result(subjectService.getAllSubjects())
                .build();
    }

    @GetMapping("/{subjectId}")
    public ApiResponse<SubjectResponse> getSubjectById(@PathVariable int subjectId) {
        return ApiResponse.<SubjectResponse>builder()
                .result(subjectService.getSubjectById(subjectId))
                .build();
    }

    @PutMapping("/{subjectId}")
    public ApiResponse<SubjectResponse> updateSubject(@PathVariable int subjectId, @RequestBody SubjectUpdateRequest request) {
        return ApiResponse.<SubjectResponse>builder()
                .result(subjectService.updateSubject(subjectId, request))
                .build();
    }

    @DeleteMapping("/{subjectId}")
    public ApiResponse<Void> deleteSubject(@PathVariable int subjectId) {
        subjectService.deleteSubject(subjectId);
        return ApiResponse.<Void>builder()
                .message("Delete subject successfully")
                .build();
    }

}
