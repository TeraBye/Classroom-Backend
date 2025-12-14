package com.example.question_service.service.Impl;

import com.example.question_service.dto.request.QuestionCreateRequest;
import com.example.question_service.dto.request.QuestionIdsRequest;
import com.example.question_service.dto.request.QuestionSearchRequest;
import com.example.question_service.dto.request.QuestionUpdateRequest;
import com.example.question_service.dto.response.ClassListResponse;
import com.example.question_service.dto.response.QuestionPagingResponse;
import com.example.question_service.dto.response.QuestionResponse;
import com.example.question_service.dto.response.SubjectResponse;
import com.example.question_service.entity.Question;
import com.example.question_service.entity.QuestionAction;
import com.example.question_service.entity.QuestionVersion;
import com.example.question_service.enums.ActionType;
import com.example.question_service.enums.Level;
import com.example.question_service.event.AuditLogEvent;
import com.example.question_service.exception.BusinessException;
import com.example.question_service.mapper.QuestionMapper;
import com.example.question_service.repository.QuestionRepository;
import com.example.question_service.repository.QuestionVersionRepository;
import com.example.question_service.repository.http.ExamClient;
import com.example.question_service.service.QuestionHistoryService;
import com.example.question_service.repository.http.ClassroomClient;
import com.example.question_service.service.QuestionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionServiceImpl implements QuestionService {
    QuestionRepository questionRepository;
    QuestionVersionRepository versionRepository;
    QuestionMapper questionMapper;
    QuestionHistoryService questionHistoryService;
    ClassroomClient classroomClient;
    ExamClient examClient;
    KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Transactional
    public QuestionResponse createQuestion(QuestionCreateRequest request) {
        Question question = questionMapper.toQuestion(request);
        question.setCreatedAt(LocalDateTime.now());
        question.setUpdatedAt(LocalDateTime.now());
        questionRepository.save(question);

        QuestionVersion version = questionMapper.toQuestionVersionFromCreateRequest(request);
        version.setQuestion(question);
        version.setUpdatedBy(request.getUsername());
        version.setVersion(1);
        version.setCreatedAt(LocalDateTime.now());
        versionRepository.save(version);

        questionHistoryService.pushAction(request.getUsername(), QuestionAction.builder()
                .type(ActionType.CREATE)
                .questionId(question.getId())
                .fromVersion(null)
                .toVersion(1)
                .build());

        AuditLogEvent logEvent = new AuditLogEvent(
                request.getUsername(),
                "TEACHER",
                "CREATE QUESTION",
                "Created new question with ID: " + question.getId() + " for subject with ID: " + request.getSubjectId()
        );
        kafkaTemplate.send("audit.log", logEvent);
        return questionMapper.toQuestionResponse(question);

    }

    @Override
    @Transactional
    public QuestionResponse updateQuestion(int questionId, QuestionUpdateRequest request) {
        Question existing = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with ID: " + questionId));

        if (examClient.isQuestionInUnstartedExam(questionId).getResult().getIsInUnstartedExam()) {
            throw new BusinessException("Cannot update question as it is part of an unstarted exam");
        }

        if (!request.getUsername().equals(existing.getUsername())) {
            throw new BusinessException("Do not have permission");
        }

        Integer currentVersion = versionRepository.findMaxVersionByQuestionId(questionId);
        if (currentVersion == null) currentVersion = 0;

        questionMapper.updateQuestion(request, existing);
        existing.setUpdatedAt(LocalDateTime.now());
        questionRepository.save(existing);

       int newVersion = currentVersion + 1;
        createNewVersion(existing, request.getUsername());

        questionHistoryService.pushAction(request.getUsername(), QuestionAction.builder()
                .type(ActionType.UPDATE)
                .questionId(questionId)
                .fromVersion(currentVersion)
                .toVersion(newVersion)
                .build());

        AuditLogEvent logEvent = new AuditLogEvent(
                request.getUsername(),
                "TEACHER",
                "UPDATE QUESTION",
                "Updated question with ID: " + questionId + ". Request: " + request.toString()
        );
        kafkaTemplate.send("audit.log", logEvent);

        return questionMapper.toQuestionResponse(existing);
    }

    @Override
    @Transactional
    public void deleteQuestion(int questionId, String username) {
        Question existing = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with ID: " + questionId));

        if (examClient.isQuestionInUnstartedExam(questionId).getResult().getIsInUnstartedExam()) {
            throw new BusinessException("Cannot delete question as it is part of an unstarted exam");
        }

        if (!username.equals(existing.getUsername())) {
            throw new BusinessException("Do not have permission");
        }

        Integer currentVersion = versionRepository.findMaxVersionByQuestionId(questionId);

        existing.setDeleted(true);
        existing.setUpdatedAt(LocalDateTime.now());
        questionRepository.save(existing);

        questionHistoryService.pushAction(username, QuestionAction.builder()
                .type(ActionType.DELETE)
                .questionId(questionId)
                .fromVersion(currentVersion)
                .toVersion(null)
                .build());

        AuditLogEvent logEvent = new AuditLogEvent(
                username,
                "TEACHER",
                "DELETE QUESTION",
                "Deleted question with ID: " + questionId
        );
        kafkaTemplate.send("audit.log", logEvent);
    }

    @Override
    @Transactional
    public String undo(String username) {
        Optional<QuestionAction> actionOptional = questionHistoryService.undo(username);
        if (actionOptional.isEmpty()) return "Nothing to undo";
        QuestionAction action = actionOptional.get();
        Integer qid = action.getQuestionId();

        switch (action.getType()) {
            case CREATE -> {
                Question question = questionRepository.findById(qid)
                        .orElseThrow(() -> new RuntimeException("Question not found with ID: " + qid));
                question.setDeleted(true);
                question.setUpdatedAt(LocalDateTime.now());
                questionRepository.save(question);
            }
            case UPDATE -> {
                Integer restoreVersion = action.getFromVersion();
                Question question = questionRepository.findById(qid)
                        .orElseThrow(() -> new RuntimeException("Question not found"));

                QuestionVersion version = versionRepository
                        .findByQuestion_IdAndVersion(qid, restoreVersion)
                        .orElseThrow(() -> new RuntimeException("Version not found: " + restoreVersion));

                questionMapper.updateFromVersion(version, question);
                question.setUpdatedAt(LocalDateTime.now());

                questionRepository.save(question);
                createNewVersion(question, username);
            }
            case DELETE -> {
                Question question = questionRepository.findById(qid)
                        .orElseThrow(() -> new RuntimeException("Question not found with ID: " + qid));
                question.setDeleted(false);
                question.setUpdatedAt(LocalDateTime.now());
                questionRepository.save(question);
                createNewVersion(question, username);
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
        Integer qid = action.getQuestionId();

        switch (action.getType()) {
            case CREATE, UPDATE -> {
                Integer targetVersion = action.getToVersion();
                if (targetVersion == null) {
                    questionRepository.deleteById(qid);
                    return "Redo delete successfully";
                }

                Question question = questionRepository.findById(qid)
                        .orElseGet(() -> {
                            Question q = new Question();
                            q.setId(qid);
                            return q;
                        });

                QuestionVersion version = versionRepository
                        .findByQuestion_IdAndVersion(qid, targetVersion)
                        .orElseThrow(() -> new RuntimeException("Version not found: " + targetVersion));

                questionMapper.updateFromVersion(version, question);
                question.setUpdatedAt(LocalDateTime.now());
                if (question.getCreatedAt() == null) {
                    question.setCreatedAt(version.getCreatedAt());
                }

                Question saved = questionRepository.save(question);
                createNewVersion(saved, username);
            }
            case DELETE -> {
                Question question = questionRepository.findById(qid)
                        .orElseThrow(() -> new RuntimeException("Question not found"));
                question.setDeleted(true);  // Redo delete = soft delete
                question.setUpdatedAt(LocalDateTime.now());
                questionRepository.save(question);
            }
        }
        return "Redo " + action.getType() + " successfully";
    }

    @Override
    public List<QuestionResponse> getQuestionsBySubjectId(int subjectId) {
        List<Question> questions = questionRepository.findBySubjectId(subjectId);
        return questionMapper.toQuestionResponses(questions);
    }

    @Override
    public QuestionPagingResponse<QuestionResponse> getPageQuestion(Integer subjectId, QuestionSearchRequest request, Pageable pageable) {
        List<Question> questionList = questionRepository.findNextPageWithFilters(subjectId, request.getLevel(), request.getKeyword(), request.getCursor(), pageable.getPageSize());
        boolean hasNext = questionList.size() == pageable.getPageSize();
        int lastCursor = questionList.isEmpty() ? request.getCursor() : questionList.get(questionList.size() - 1).getId();
        return new QuestionPagingResponse<>(questionMapper.toQuestionResponses(questionList), lastCursor, hasNext);
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

    //Luan lam
    @Override
    public List<QuestionResponse> getRandomQuestionsBySubject(
            Integer subjectId,
            int n,
            double hardRatio,
            double mediumRatio,
            double easyRatio
    ) throws IllegalStateException {

        // Validate tổng 3 tỷ lệ = 1
        if (Math.abs((hardRatio + mediumRatio + easyRatio) - 1.0f) > 0.0001) {
            throw new IllegalArgumentException("Tổng hardRatio + mediumRatio + easyRatio phải bằng 1");
        }

        int minHard = (int) Math.ceil(n * hardRatio);
        int maxEasy = (int) Math.floor(n * easyRatio);

        int remaining = n - minHard;
        int maxMedium = remaining - maxEasy;

        long availableHard = questionRepository.countBySubjectIdAndLevel(subjectId, Level.HARD);
        long availableEasy = questionRepository.countBySubjectIdAndLevel(subjectId, Level.EASY);
        long availableMedium = questionRepository.countBySubjectIdAndLevel(subjectId, Level.MEDIUM);

        if (availableHard < minHard) {
            throw new IllegalStateException("Không đủ câu HARD: cần " + minHard + ", có " + availableHard);
        }
        if (availableEasy < 1 && maxEasy > 0) {
            throw new IllegalStateException("Không đủ câu EASY: cần ≥1, có " + availableEasy);
        }

        int easyCount = Math.min(maxEasy, (int) availableEasy);
        int mediumCount = n - minHard - easyCount;

        if (availableMedium < mediumCount) {
            throw new IllegalStateException(
                    "Không đủ câu MEDIUM: cần " + mediumCount + ", có " + availableMedium
            );
        }

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
        return questionMapper.toQuestionResponses(
                questionRepository.findByIdIn(request.getQuestionIds())
        );
    }

    @Override
    public void deleteQuestionById(int questionId) {
        Question before = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with ID: " + questionId));

        questionRepository.deleteById(questionId);
    }

    private void createNewVersion(Question question, String updatedBy) {
        Integer maxVersion = versionRepository.findMaxVersionByQuestionId(question.getId());
        int latest = (maxVersion != null) ? maxVersion : 0;

        QuestionVersion newVersion = questionMapper.toQuestionVersion(question);
        newVersion.setQuestion(question);
        newVersion.setVersion(latest + 1);
        newVersion.setUpdatedBy(updatedBy);
        newVersion.setCreatedAt(LocalDateTime.now());
        versionRepository.save(newVersion);
    }

    @Override
    public List<QuestionResponse> getQuestionsByIds(List<Integer> questionIds) {
        List<Question> questions = questionRepository.findAllById(questionIds);
        return questionMapper.toQuestionResponses(questions);
    }
}
