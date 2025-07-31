package com.example.exam_service.controller;

import com.example.exam_service.dto.request.ExamCreationRequest;
import com.example.exam_service.dto.response.ApiResponse;
import com.example.exam_service.dto.response.ExamResponse;
import com.example.exam_service.service.ExamService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExamController {
    ExamService examService;

    @PostMapping("/createExam")
    public ApiResponse<ExamResponse> createExam(@RequestBody ExamCreationRequest request){
        return ApiResponse.<ExamResponse>builder()
                .result(examService.createExam(request))
                .build();
    }
}
