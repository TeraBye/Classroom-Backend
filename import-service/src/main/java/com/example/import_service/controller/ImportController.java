package com.example.import_service.controller;

import com.example.import_service.dto.response.ApiResponse;
import com.example.import_service.dto.response.ImportJobResponse;
import com.example.import_service.service.ImportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImportController {
    ImportService importService;

    @PostMapping("/questions")
    public ApiResponse<Map<String, Object>> importQuestions(
            @RequestParam("file") MultipartFile file,
            @RequestParam("username") String username
    ) {
        Long jobId = importService.handleImport(file, username, "question");
        return ApiResponse.<Map<String, Object>>builder()
                .result(Map.of("jobId", jobId))
                .build();
    }

    @GetMapping("/jobs/{jobId}")
    public ApiResponse<ImportJobResponse> getJobStatus(@PathVariable Long jobId) {
        return ApiResponse.<ImportJobResponse>builder()
                .result(importService.getJobStatus(jobId))
                .build();
    }
}
