package com.example.exam_service.service.impl;

import com.example.exam_service.dto.request.ExamSubmissionRequest;
import com.example.exam_service.dto.request.QuestionIdsRequest;
import com.example.exam_service.dto.request.UpdateAnswerRequest;
import com.example.exam_service.dto.response.ExamSubmissionResponse;
import com.example.exam_service.dto.response.ExamSubmissionViewResponse;
import com.example.exam_service.dto.response.QuestionResponse;
import com.example.exam_service.entity.ExamQuestion;
import com.example.exam_service.entity.ExamSubmission;
import com.example.exam_service.entity.ExamSubmissionAnswer;
import com.example.exam_service.mapper.ExamSubmissionMapper;
import com.example.exam_service.repository.ExamAnswerRepository;
import com.example.exam_service.repository.ExamQuestionRepository;
import com.example.exam_service.repository.ExamRepository;
import com.example.exam_service.repository.ExamSubmissionRepository;
import com.example.exam_service.repository.httpClient.QuestionClient;
import com.example.exam_service.service.ExamSubmissionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ExamSubmissionServiceImpl implements ExamSubmissionService {
    ExamRepository examRepository;
    ExamQuestionRepository examQuestionRepository;
    ExamSubmissionRepository examSubmissionRepository;
    ExamAnswerRepository examAnswerRepository;
    ExamSubmissionMapper examSubmissionMapper;
    private final QuestionClient questionClient;

    @Override
    public ExamSubmissionViewResponse getExamForStudents(ExamSubmissionRequest request){
        ExamSubmission examSubmission =
                examSubmissionMapper.toExamSubmission(request);
        examSubmission.setExam(examRepository.findById(request.getExamId()).orElse(null));
        examSubmission =  examSubmissionRepository.save(examSubmission);

        List<Integer>questionIds = createSubmissionAnswers(
                examQuestionRepository.findByExamId(request.getExamId()), examSubmission
        );

        List<QuestionResponse> questionResponses = questionClient.getQuestionsByIds(
                QuestionIdsRequest.builder()
                        .questionIds(questionIds)
                        .build()
        ).getResult();

        return ExamSubmissionViewResponse.builder()
                .examSubmission(examSubmission)
                .questionResponses(questionResponses)
                .build();
    }

    public List<Integer> createSubmissionAnswers(
            List<ExamQuestion> examQuestions,
            ExamSubmission submission
    ) {
        List<ExamSubmissionAnswer> answers = new ArrayList<>();
        List<Integer> questionIds = new ArrayList<>();

        for (ExamQuestion eq : examQuestions) {
            int qId = eq.getQuestionId();
            questionIds.add(qId);

            ExamSubmissionAnswer answer = new ExamSubmissionAnswer();
            answer.setQuestionId(qId);
            answer.setSelectedOption(null);
            answer.setSubmission(submission);
            answers.add(answer);
        }

        examAnswerRepository.saveAll(answers);

        return questionIds;
    }

    @Override
    public void updateSelectedOption(UpdateAnswerRequest request) {
        ExamSubmissionAnswer answer = examAnswerRepository
                .findBySubmissionIdAndQuestionId(request.getExamSubmissionId(), request.getQuestionId())
                .orElseThrow(() -> new EntityNotFoundException("Answer not found"));

        answer.setSelectedOption(request.getSelectedOption());
        examAnswerRepository.save(answer);
    }

    @Override
    public ExamSubmissionResponse updateExamSubmission(String student, Long examId){
        ExamSubmission examSubmission = examSubmissionRepository.findByStudentAndExamId(student, examId);
        int numberOfCorrectAnswers = calculateScore(examSubmission);
        ExamSubmissionResponse examSubmissionResponse = examSubmissionMapper
                .toExamSubmissionResponse(examSubmission);
        examSubmissionResponse.setNumberOfCorrectAnswers(numberOfCorrectAnswers);
        return examSubmissionResponse;

    }

    public int calculateScore(ExamSubmission examSubmission){
        List<ExamSubmissionAnswer> answers = examAnswerRepository.findAllBySubmissionId(examSubmission.getId());
        List<Integer> questionIds = answers.stream()
                .map(ExamSubmissionAnswer::getQuestionId)
                .collect(Collectors.toList());

        List<QuestionResponse> questions = questionClient.getQuestionsByIds(
                QuestionIdsRequest.builder()
                        .questionIds(questionIds)
                        .build()
        ).getResult();

        Map<Integer, String> answerMap = answers.stream()
                .collect(Collectors.toMap(
                        ExamSubmissionAnswer::getQuestionId,
                        a -> a.getSelectedOption() != null ? a.getSelectedOption() : "F",
                        (oldVal, newVal) -> newVal
                ));


        int correctCount = 0;

        for (QuestionResponse q : questions) {
            String selected = answerMap.get(q.getId());
            String correct = q.getCorrectAnswer();

            if (selected != null && selected.equalsIgnoreCase(correct)) {
                correctCount++;
            }
        }
        examSubmission.setSubmittedAt(LocalDateTime.now());
        examSubmission.setScore(calculateScore(correctCount, answers.size()));
        examSubmissionRepository.save(examSubmission);
        return correctCount;
    }

    public float calculateScore(int correctCount, int totalQuestions) {
        if (totalQuestions == 0) return 0f;

        float rawScore = ((float) correctCount / totalQuestions) * 10;

        // Làm tròn đến 2 chữ số sau dấu phẩy
        BigDecimal rounded = BigDecimal.valueOf(rawScore)
                .setScale(2, RoundingMode.HALF_UP);

        return rounded.floatValue();
    }



}
