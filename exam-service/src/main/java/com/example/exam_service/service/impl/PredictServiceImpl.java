package com.example.exam_service.service.impl;

import com.example.exam_service.dto.response.ExamSubmissionResponse;
import com.example.exam_service.dto.response.QuestionResponse;
import com.example.exam_service.dto.request.QuestionIdsRequest;
import com.example.exam_service.entity.ExamSubmissionAnswer;
import com.example.exam_service.repository.ExamAnswerRepository;
import com.example.exam_service.repository.httpClient.QuestionClient;
import com.example.exam_service.service.ExamSubmissionService;
import com.example.exam_service.service.PredictService;
import com.example.exam_service.enums.Level;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.example.exam_service.dto.response.PredictResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PredictServiceImpl implements PredictService {

    ExamSubmissionService examSubmissionService;
    ExamAnswerRepository examAnswerRepository;
    QuestionClient questionClient;

    public Double getAvgRecentScores(String student) {
        List<ExamSubmissionResponse> examSubmissionResponseList = examSubmissionService.getRecentExamSubmissionsByStudentId(student);
        return examSubmissionResponseList.stream()
                .filter(submission -> submission.getScore() != null)
                .mapToDouble(ExamSubmissionResponse::getScore)
                .average()
                .orElse(0.0);
    }

    public Double getHardCorrectRatio(String student) {
        List<ExamSubmissionResponse> submissions = examSubmissionService.getRecentExamSubmissionsByStudentId(student);
        return calculateCorrectRatioByLevel(submissions, Level.HARD);
    }

    public Double getMediumCorrectRatio(String student) {
        List<ExamSubmissionResponse> submissions = examSubmissionService.getRecentExamSubmissionsByStudentId(student);
        return calculateCorrectRatioByLevel(submissions, Level.MEDIUM);
    }

    public Double getEasyCorrectRatio(String student) {
        List<ExamSubmissionResponse> submissions = examSubmissionService.getRecentExamSubmissionsByStudentId(student);
        return calculateCorrectRatioByLevel(submissions, Level.EASY);
    }

    public Integer getHardQuestionsAttempted(String student) {
        List<ExamSubmissionResponse> submissions = examSubmissionService.getRecentExamSubmissionsByStudentId(student);
        return countQuestionsByLevel(submissions, Level.HARD);
    }

    public Double getExamTrend(String student) {
        List<ExamSubmissionResponse> submissions = examSubmissionService.getRecentExamSubmissionsByStudentId(student);
        if (submissions.size() < 2) return 0.0;

        double firstHalf = submissions.stream()
                .limit(submissions.size() / 2)
                .filter(s -> s.getScore() != null)
                .mapToDouble(ExamSubmissionResponse::getScore)
                .average()
                .orElse(0.0);

        double secondHalf = submissions.stream()
                .skip(submissions.size() / 2)
                .filter(s -> s.getScore() != null)
                .mapToDouble(ExamSubmissionResponse::getScore)
                .average()
                .orElse(0.0);

        return secondHalf - firstHalf; // Positive means improving, negative means declining
    }

    public Double getAvgTimePerQuestion(String student) {
        List<ExamSubmissionResponse> submissions = examSubmissionService.getRecentExamSubmissionsByStudentId(student);

        return submissions.stream()
                .filter(s -> s.getExamTime() != null && s.getExamTime() > 0)
                .mapToDouble(s -> {
                    // Get question count for this submission
                    int questionCount = getQuestionCountForSubmission(s.getId());
                    return questionCount > 0 ? (double) s.getExamTime() / questionCount : 0.0;
                })
                .average()
                .orElse(0.0);
    }

    public Double getConsistency(String student) {
        List<ExamSubmissionResponse> submissions = examSubmissionService.getRecentExamSubmissionsByStudentId(student);

        List<Double> scores = submissions.stream()
                .filter(s -> s.getScore() != null)
                .map(s -> s.getScore().doubleValue())
                .collect(Collectors.toList());

        if (scores.size() < 2) return 0.0;

        double mean = scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = scores.stream()
                .mapToDouble(score -> Math.pow(score - mean, 2))
                .average()
                .orElse(0.0);

        double stdDev = Math.sqrt(variance);
        return mean > 0 ? (1.0 - (stdDev / mean)) : 0.0; // Higher value means more consistent
    }

    public Integer getRecentStreak(String student) {
        List<ExamSubmissionResponse> submissions = examSubmissionService.getRecentExamSubmissionsByStudentId(student);

        int streak = 0;
        boolean isPositiveStreak = true; // Track if we're counting positive or negative streak

        for (ExamSubmissionResponse submission : submissions) {
            if (submission.getScore() == null) continue;

            if (streak == 0) {
                // First score determines streak direction
                isPositiveStreak = submission.getScore() >= 7.0; // Assuming 7.0 is passing grade
                streak = 1;
            } else {
                // Continue streak if condition matches
                boolean isGoodScore = submission.getScore() >= 7.0;
                if (isGoodScore == isPositiveStreak) {
                    streak++;
                } else {
                    break; // Streak broken
                }
            }
        }

        return isPositiveStreak ? streak : -streak; // Negative for bad streaks
    }

    // Helper methods
    private Double calculateCorrectRatioByLevel(List<ExamSubmissionResponse> submissions, Level level) {
        int totalQuestions = 0;
        int correctAnswers = 0;

        for (ExamSubmissionResponse submission : submissions) {
            List<ExamSubmissionAnswer> answers = examAnswerRepository.findAllBySubmissionId(submission.getId());

            List<Integer> questionIds = answers.stream()
                    .map(ExamSubmissionAnswer::getQuestionId)
                    .collect(Collectors.toList());

            if (questionIds.isEmpty()) continue;

            List<QuestionResponse> questions = questionClient.getQuestionsByIds(
                    QuestionIdsRequest.builder().questionIds(questionIds).build()
            ).getResult();

            Map<Integer, String> answerMap = answers.stream()
                    .collect(Collectors.toMap(
                            ExamSubmissionAnswer::getQuestionId,
                            answer -> answer.getSelectedOption() != null ? answer.getSelectedOption() : "F"
                    ));

            for (QuestionResponse question : questions) {
                if (Level.valueOf(question.getLevel()) == level) {
                    totalQuestions++;
                    String selectedAnswer = answerMap.get(question.getId());
                    if (selectedAnswer != null && selectedAnswer.equalsIgnoreCase(question.getCorrectAnswer())) {
                        correctAnswers++;
                    }
                }
            }
        }

        return totalQuestions > 0 ? (double) correctAnswers / totalQuestions : 0.0;
    }

    private Integer countQuestionsByLevel(List<ExamSubmissionResponse> submissions, Level level) {
        int count = 0;

        for (ExamSubmissionResponse submission : submissions) {
            List<ExamSubmissionAnswer> answers = examAnswerRepository.findAllBySubmissionId(submission.getId());

            List<Integer> questionIds = answers.stream()
                    .map(ExamSubmissionAnswer::getQuestionId)
                    .collect(Collectors.toList());

            if (questionIds.isEmpty()) continue;

            List<QuestionResponse> questions = questionClient.getQuestionsByIds(
                    QuestionIdsRequest.builder().questionIds(questionIds).build()
            ).getResult();

            count += (int) questions.stream()
                    .filter(q -> Level.valueOf(q.getLevel()) == level)
                    .count();
        }

        return count;
    }

    private int getQuestionCountForSubmission(Long submissionId) {
        List<ExamSubmissionAnswer> answers = examAnswerRepository.findAllBySubmissionId(submissionId);
        return answers.size();
    }

    public PredictResponse getPredictData(String student) {
        return PredictResponse.builder()
                .avgRecentScore(getAvgRecentScores(student).floatValue())
                .hardCorrectRatio(getHardCorrectRatio(student).floatValue())
                .mediumCorrectRatio(getMediumCorrectRatio(student).floatValue())
                .easyCorrectRatio(getEasyCorrectRatio(student).floatValue())
                .hardQuestionsAttempted(getHardQuestionsAttempted(student))
                .examTrend(getExamTrend(student).floatValue())
                .avgTimePerQuestion(getAvgTimePerQuestion(student).floatValue())
                .consistency(getConsistency(student).floatValue())
                .recentStreak(getRecentStreak(student))
                .build();
    }
}
