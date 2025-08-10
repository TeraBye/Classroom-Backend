package com.example.question_service.service.Impl;

import com.example.question_service.dto.request.QuestionCreateRequest;
import com.example.question_service.dto.request.QuestionIdsRequest;
import com.example.question_service.dto.request.QuestionUpdateRequest;
import com.example.question_service.dto.response.ClassListResponse;
import com.example.question_service.dto.response.QuestionPagingResponse;
import com.example.question_service.dto.response.QuestionResponse;
import com.example.question_service.dto.response.SubjectResponse;
import com.example.question_service.entity.Question;
import com.example.question_service.entity.QuestionAction;
import com.example.question_service.enums.ActionType;
import com.example.question_service.enums.Level;
import com.example.question_service.exception.BusinessException;
import com.example.question_service.mapper.QuestionMapper;
import com.example.question_service.repository.QuestionRepository;
import com.example.question_service.repository.http.ClassroomClient;
import com.example.question_service.service.QuestionHistoryService;
import com.example.question_service.service.QuestionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionServiceImpl implements QuestionService {
    QuestionRepository questionRepository;
    QuestionMapper questionMapper;
    ClassroomClient classroomClient;
    QuestionHistoryService questionHistoryService;
    ObjectMapper objectMapper;

    @Override
    @Transactional
    public QuestionResponse createQuestion(QuestionCreateRequest request) {
        if (!questionRepository.existsByContentIgnoreCase(request.getContent())) {
            Question question = questionMapper.toQuestion(request);
            question.setCreatedAt(LocalDateTime.now());
            questionHistoryService.pushAction(request.getUsername(),
                    new QuestionAction(ActionType.CREATE, null, question));
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
        return questionMapper.toQuestionResponses(questionRepository.findAll());
    }

    @Override
    @Transactional
    public QuestionResponse updateQuestion(int questionId, QuestionUpdateRequest request) throws JsonProcessingException {
        Question existing = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with ID: " + questionId));

        Question before = objectMapper.readValue(objectMapper.writeValueAsString(existing), Question.class);
        questionMapper.updateQuestion(request, existing);
        existing.setUpdatedAt(LocalDateTime.now());

        questionHistoryService.pushAction(request.getUsername(),
                new QuestionAction(ActionType.UPDATE, before, existing));
        return questionMapper.toQuestionResponse(questionRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteQuestion(int questionId) {
        Question before = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with ID: " + questionId));
        questionRepository.deleteById(questionId);
        questionHistoryService.pushAction("system",
                new QuestionAction(ActionType.DELETE, before, null));
    }

    @Override
    @Transactional
    public String undo(String username) {
        Optional<QuestionAction> actionOptional = questionHistoryService.undo(username);
        if (actionOptional.isEmpty()) return "Nothing to undo";
        QuestionAction action = actionOptional.get();
        switch (action.getType()) {
            case CREATE -> {
                Question after = action.getAfter();
                if (after.getId() != null && questionRepository.existsById(after.getId())) {
                    questionRepository.deleteById(after.getId());
                }
            }
            case UPDATE -> {
                Question before = action.getBefore();
                Question existing = questionRepository.findById(before.getId())
                        .orElseThrow(() -> new RuntimeException("Cannot undo update: entity missing"));
                questionMapper.updateQuestion(questionMapper.toUpdateRequest(before), existing);
                existing.setUpdatedAt(LocalDateTime.now());
                questionRepository.save(existing);
            }
            case DELETE -> {
                Question before = action.getBefore();
                before.setId(null);
                questionRepository.save(before);
            }
        }
        return "Undo " + action.getType() + " successfully";
    }

    @Override
    @Transactional
    public String redo(String username) {
        Optional<QuestionAction> actionOptional = questionHistoryService.redo(username);
        if (actionOptional.isEmpty()) return "Nothing to redo";
        QuestionAction action = actionOptional.get();
        switch (action.getType()) {
            case CREATE -> {
                Question after = action.getAfter();
                after.setId(null);
                questionRepository.save(after);
            }
            case UPDATE -> {
                Question after = action.getAfter();
                Question existing = questionRepository.findById(after.getId())
                        .orElseThrow(() -> new RuntimeException("Cannot redo update: entity missing"));
                questionMapper.updateQuestion(questionMapper.toUpdateRequest(after), existing);
                existing.setUpdatedAt(LocalDateTime.now());
                questionRepository.save(existing);
            }
            case DELETE -> {
                Question before = action.getBefore();
                if (before.getId() != null && questionRepository.existsById(before.getId())) {
                    questionRepository.deleteById(before.getId());
                }
            }
        }
        return "Redo " + action.getType() + " successfully";
    }

    @Override
    public List<QuestionResponse> getQuestionsBySubjectId(int subjectId) {
        return questionMapper.toQuestionResponses(questionRepository.findBySubjectId(subjectId));
    }

    @Override
    public QuestionPagingResponse<QuestionResponse> getPageQuestion(Integer subjectId, int cursor, Pageable pageable) {
        List<Question> questionList = questionRepository.findNextPageScore(subjectId, cursor, pageable);
        boolean hasNext = questionList.size() == pageable.getPageSize();
        int lastCursor = questionList.isEmpty() ? cursor : questionList.getLast().getId();
        return new QuestionPagingResponse<>(questionMapper.toQuestionResponses(questionList), lastCursor, hasNext);
    }

    @Override
    public List<ClassListResponse> getSubjectList(Integer subjectId, int cursor, Pageable pageable) {
        List<Integer> listSubject;
        List<SubjectResponse> subjectResponses;

        if (subjectId == -999) {
            listSubject = questionRepository.findAllNextPageSubjectQuestion(cursor, pageable);
        } else {
            listSubject = questionRepository.findDistinctNextPageListSubject(subjectId, cursor, pageable);
        }

        try {
            subjectResponses = classroomClient.getListSubject(listSubject).getResult();
        } catch (Exception e) {
            throw new BusinessException("Can not get classes name: " + e.getMessage());
        }

        if (listSubject != null && subjectResponses != null) {
            List<ClassListResponse> listResponses = new ArrayList<>();
            for (SubjectResponse subject : subjectResponses) {
                int total = questionRepository.countBySubjectId(subject.getId());
                listResponses.add(new ClassListResponse(subject.getId(), subject.getName(), total));
            }
            return listResponses;
        }
        throw new BusinessException("Can not find any subject!");
    }

    @Override
    public List<QuestionResponse> getRandomQuestionsBySubject(Integer subjectId, int n) {
        int minHard = (int) Math.ceil(n * 0.2);
        int maxEasy = (int) Math.floor(n * 0.3);
        int remaining = n - minHard;
        int maxMedium = remaining - maxEasy;

        long availableHard = questionRepository.countBySubjectIdAndLevel(subjectId, Level.HARD);
        long availableEasy = questionRepository.countBySubjectIdAndLevel(subjectId, Level.EASY);
        long availableMedium = questionRepository.countBySubjectIdAndLevel(subjectId, Level.MEDIUM);

        if (availableHard < minHard) {
            throw new IllegalStateException("Không đủ câu hỏi HARD: Cần ít nhất " + minHard + ", chỉ có " + availableHard);
        }
        if (availableEasy < 1 && maxEasy > 0) {
            throw new IllegalStateException("Không đủ câu hỏi EASY: Cần ít nhất 1, chỉ có " + availableEasy);
        }
        if (availableMedium < (n - minHard - Math.min(availableEasy, maxEasy))) {
            throw new IllegalStateException("Không đủ câu hỏi MEDIUM: Cần ít nhất " +
                    (n - minHard - Math.min(availableEasy, maxEasy)) + ", chỉ có " + availableMedium);
        }

        int easyCount = Math.min(maxEasy, (int) availableEasy);
        int mediumCount = n - minHard - easyCount;

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
    public List<QuestionResponse> getQuestionByIds(QuestionIdsRequest request) {
        return questionMapper.toQuestionResponses(questionRepository.findByIdIn(request.getQuestionIds()));
    }
}
