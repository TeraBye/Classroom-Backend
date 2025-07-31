package com.example.exam_service.service;

import com.example.exam_service.dto.request.ExamCreationRequest;
import com.example.exam_service.dto.response.ExamResponse;

public interface ExamService {
    ExamResponse createExam(ExamCreationRequest examCreationRequest);
}
