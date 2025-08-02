package com.example.exam_service.service;

import com.example.exam_service.dto.request.ExamSubmissionRequest;
import com.example.exam_service.dto.request.UpdateAnswerRequest;
import com.example.exam_service.dto.response.ExamSubmissionResponse;
import com.example.exam_service.dto.response.ExamSubmissionViewResponse;

public interface ExamSubmissionService {
    ExamSubmissionViewResponse getExamForStudents(ExamSubmissionRequest request);
    void updateSelectedOption(UpdateAnswerRequest request);
    ExamSubmissionResponse updateExamSubmission(String student, Long examId);
}
