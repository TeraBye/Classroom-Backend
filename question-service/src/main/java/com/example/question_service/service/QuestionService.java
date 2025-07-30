package com.example.question_service.service;

import com.example.question_service.dto.request.QuestionCreateRequest;
import com.example.question_service.dto.request.QuestionUpdateRequest;
import com.example.question_service.dto.response.ClassListResponse;
import com.example.question_service.dto.response.QuestionPagingResponse;
import com.example.question_service.dto.response.QuestionResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {
    QuestionResponse createQuestion(QuestionCreateRequest request);
    QuestionResponse getQuestionById(int questionId);
    List<QuestionResponse> getAllQuestions();
    QuestionResponse updateQuestion(int questionId, QuestionUpdateRequest request);
    void deleteQuestion(int questionId);
    List<QuestionResponse> getQuestionsBySubjectId(int subjectId);

    QuestionPagingResponse<QuestionResponse> getPageQuestion(Integer subjectId, int cursor, Pageable pageable);

    List<ClassListResponse> getSubjectList(Integer subjectId, int cursor, Pageable pageable);
}
