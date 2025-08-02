package com.example.exam_service.service;

import com.example.exam_service.dto.request.ExamCreationRequest;
import com.example.exam_service.dto.response.ExamResponse;
import com.example.exam_service.dto.response.ExamViewResponse;
import com.example.exam_service.dto.response.QuestionResponse;
import com.example.exam_service.entity.Exam;

import java.util.List;

public interface ExamService {
    ExamViewResponse createExam(ExamCreationRequest request);
    void createExamQuestions(Exam exam, List<QuestionResponse> questionList);
}
