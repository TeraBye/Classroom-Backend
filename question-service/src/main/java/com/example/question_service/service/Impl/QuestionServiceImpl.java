package com.example.question_service.service.Impl;

import com.example.question_service.dto.request.QuestionCreateRequest;
import com.example.question_service.dto.request.QuestionIdsRequest;
import com.example.question_service.dto.request.QuestionUpdateRequest;
import com.example.question_service.dto.response.QuestionResponse;
import com.example.question_service.entity.Question;
import com.example.question_service.enums.Level;
import com.example.question_service.mapper.QuestionMapper;
import com.example.question_service.repository.QuestionRepository;
import com.example.question_service.service.QuestionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    //Luan lam
    @Override
    public List<QuestionResponse> getRandomQuestionsBySubject(Integer subjectId, int n) throws IllegalStateException {
        // Tính số lượng câu hỏi HARD và EASY
        int minHard = (int) Math.ceil(n * 0.2); // Ít nhất 20% là HARD
        int maxEasy = (int) Math.floor(n * 0.3); // Nhiều nhất 30% là EASY
        int remaining = n - minHard; // Số câu hỏi còn lại (EASY + MEDIUM)
        int maxMedium = remaining - maxEasy; // Số câu MEDIUM tối đa

        // Kiểm tra số lượng câu hỏi có sẵn trong cơ sở dữ liệu
        long availableHard = questionRepository.countBySubjectIdAndLevel(subjectId, Level.HARD);
        long availableEasy = questionRepository.countBySubjectIdAndLevel(subjectId, Level.EASY);
        long availableMedium = questionRepository.countBySubjectIdAndLevel(subjectId, Level.MEDIUM);

        // Kiểm tra điều kiện
        if (availableHard < minHard) {
            throw new IllegalStateException("Không đủ câu hỏi HARD: Cần ít nhất " + minHard + ", chỉ có " + availableHard);
        }
        if (availableEasy < 1 && maxEasy > 0) { // Nếu maxEasy > 0 nhưng không có EASY
            throw new IllegalStateException("Không đủ câu hỏi EASY: Cần ít nhất 1, chỉ có " + availableEasy);
        }
        if (availableMedium < (n - minHard - Math.min(availableEasy, maxEasy))) {
            throw new IllegalStateException("Không đủ câu hỏi MEDIUM: Cần ít nhất " + (n - minHard - Math.min(availableEasy, maxEasy)) + ", chỉ có " + availableMedium);
        }

        // Lấy số câu EASY (tối đa maxEasy, nhưng không vượt quá availableEasy)
        int easyCount = Math.min(maxEasy, (int) availableEasy);
        int mediumCount = n - minHard - easyCount; // Số câu MEDIUM cần lấy

        // Lấy câu hỏi ngẫu nhiên
        List<Question> questions = new ArrayList<>();
        questions.addAll(questionRepository.findRandomBySubjectIdAndLevel(subjectId, Level.HARD, PageRequest.of(0, minHard)));
        if (easyCount > 0) {
            questions.addAll(questionRepository.findRandomBySubjectIdAndLevel(subjectId, Level.EASY, PageRequest.of(0, easyCount)));
        }
        if (mediumCount > 0) {
            questions.addAll(questionRepository.findRandomBySubjectIdAndLevel(subjectId, Level.MEDIUM, PageRequest.of(0, mediumCount)));
        }

        return questionMapper.toQuestionResponses(questions);
    }

    @Override
    public List<QuestionResponse> getQuestionByIds(QuestionIdsRequest request){
        return  questionMapper.toQuestionResponses(
                questionRepository.findByIdIn(request.getQuestionIds())
        );
    }
}
