package com.example.exam_service.service;

import com.example.exam_service.dto.request.ExamSubmissionRequest;
import com.example.exam_service.dto.request.UpdateAnswerRequest;
import com.example.exam_service.dto.response.ExamSubmissionResponse;
import com.example.exam_service.dto.response.ExamSubmissionViewResponse;
import com.example.exam_service.dto.response.FinalStudentExamViewResponse;
import com.example.exam_service.dto.response.ProblemExamCheck;

import java.util.List;

public interface ExamSubmissionService {
    ExamSubmissionViewResponse getExamForStudents(ExamSubmissionRequest request);
    void updateSelectedOption(UpdateAnswerRequest request);
    ExamSubmissionResponse updateExamSubmission(String student, Long examId);
    ExamSubmissionViewResponse getExamSubmission(String student, Long examId);
    List<ExamSubmissionResponse> getExamsByClass(Long classId);
    FinalStudentExamViewResponse getStudentAnswer(String student, Long examId);
    ProblemExamCheck isProblemExam(String student, Long examId);
    List<ExamSubmissionResponse> getRecentExamSubmissionsByStudentId(String student);
}
