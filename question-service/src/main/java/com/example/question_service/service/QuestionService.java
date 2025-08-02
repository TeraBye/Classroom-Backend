package com.example.question_service.service;

import com.example.question_service.dto.request.QuestionCreateRequest;
import com.example.question_service.dto.request.QuestionIdsRequest;
import com.example.question_service.dto.request.QuestionUpdateRequest;
import com.example.question_service.dto.response.QuestionResponse;
import com.example.question_service.entity.Question;

import java.util.List;

public interface QuestionService {
    QuestionResponse createQuestion(QuestionCreateRequest request);
    QuestionResponse getQuestionById(int questionId);
    List<QuestionResponse> getAllQuestions();
    QuestionResponse updateQuestion(int questionId, QuestionUpdateRequest request);
    void deleteQuestion(int questionId);
    //Luan lam
    List<QuestionResponse> getQuestionsBySubjectId(int subjectId);
    List<QuestionResponse> getRandomQuestionsBySubject(Integer subjectId, int n) throws IllegalStateException;
    List<QuestionResponse> getQuestionByIds(QuestionIdsRequest request);
}
