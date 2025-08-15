package com.example.question_service.service;

import com.example.question_service.dto.request.QuestionCreateRequest;
import com.example.question_service.dto.request.QuestionIdsRequest;
import com.example.question_service.dto.request.QuestionUpdateRequest;
import com.example.question_service.dto.response.ClassListResponse;
import com.example.question_service.dto.response.QuestionPagingResponse;
import com.example.question_service.dto.response.QuestionResponse;
import com.example.question_service.entity.Question;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {
    QuestionResponse createQuestion(QuestionCreateRequest request);
    QuestionResponse getQuestionById(int questionId);
    List<QuestionResponse> getAllQuestions();
    QuestionResponse updateQuestion(int questionId, QuestionUpdateRequest request) throws JsonProcessingException;
    void deleteQuestion(int questionId, String username);
    String undo(String username);
    String redo(String username);
    //Luan lam
    List<QuestionResponse> getQuestionsBySubjectId(int subjectId);
    List<QuestionResponse> getRandomQuestionsBySubject(Integer subjectId, int n) throws IllegalStateException;
    List<QuestionResponse> getQuestionByIds(QuestionIdsRequest request);

    QuestionPagingResponse<QuestionResponse> getPageQuestion(Integer subjectId, int cursor, Pageable pageable);

    List<ClassListResponse> getSubjectList(Integer subjectId, int cursor, Pageable pageable);

    void deleteQuestionById(int questionId);
}
