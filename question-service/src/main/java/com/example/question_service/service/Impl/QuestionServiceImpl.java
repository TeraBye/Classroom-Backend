package com.example.question_service.service.Impl;

import com.example.question_service.dto.request.QuestionCreateRequest;
import com.example.question_service.dto.request.QuestionUpdateRequest;
import com.example.question_service.dto.response.QuestionResponse;
import com.example.question_service.entity.Question;
import com.example.question_service.mapper.QuestionMapper;
import com.example.question_service.repository.QuestionRepository;
import com.example.question_service.service.QuestionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionServiceImpl implements QuestionService {
    QuestionRepository questionRepository;
    QuestionMapper questionMapper;
    @Override
    public QuestionResponse createQuestion(QuestionCreateRequest request) {
        if (!questionRepository.existsByContentIgnoreCase(request.getContent())) {
            Question question = questionMapper.toQuestion(request);
            question.setCreatedAt(LocalDateTime.now());
            return questionMapper.toQuestionResponse(questionRepository.save(question));
        } else {
            throw new RuntimeException("Question existed");
        }
    }

    @Override
    public QuestionResponse getQuestionById(int questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with ID: " + questionId));
        return questionMapper.toQuestionResponse(question);
    }

    @Override
    public List<QuestionResponse> getAllQuestions() {
        List<Question> questions = questionRepository.findAll();
        return questionMapper.toQuestionResponses(questions);
    }

    @Override
    public QuestionResponse updateQuestion(int questionId, QuestionUpdateRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with ID: " + questionId));
        questionMapper.updateQuestion(request,question);
        question.setUpdatedAt(LocalDateTime.now());
        return questionMapper.toQuestionResponse(questionRepository.save(question));
    }

    @Override
    public void deleteQuestion(int questionId) {
        questionRepository.deleteById(questionId);
    }

    @Override
    public List<QuestionResponse> getQuestionsBySubjectId(int subjectId) {
        List<Question> questions = questionRepository.findBySubjectId(subjectId);
        return questionMapper.toQuestionResponses(questions);
    }
}
