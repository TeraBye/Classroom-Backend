package com.example.exam_service.controller;

import com.example.exam_service.dto.response.ApiResponse;
import com.example.exam_service.dto.response.PredictResponse;
import com.example.exam_service.service.PredictService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/predict")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PredictController {

    PredictService predictService;

    @GetMapping("/analytics")
    public ApiResponse<PredictResponse> getPredictAnalytics(@RequestParam String student) {
        return ApiResponse.<PredictResponse>builder()
                .result(predictService.getPredictData(student))
                .build();
    }

    @GetMapping("/avg-recent-score")
    public ApiResponse<Double> getAvgRecentScore(@RequestParam String student) {
        return ApiResponse.<Double>builder()
                .result(predictService.getAvgRecentScores(student))
                .build();
    }

    @GetMapping("/hard-correct-ratio")
    public ApiResponse<Double> getHardCorrectRatio(@RequestParam String student) {
        return ApiResponse.<Double>builder()
                .result(predictService.getHardCorrectRatio(student))
                .build();
    }

    @GetMapping("/medium-correct-ratio")
    public ApiResponse<Double> getMediumCorrectRatio(@RequestParam String student) {
        return ApiResponse.<Double>builder()
                .result(predictService.getMediumCorrectRatio(student))
                .build();
    }

    @GetMapping("/easy-correct-ratio")
    public ApiResponse<Double> getEasyCorrectRatio(@RequestParam String student) {
        return ApiResponse.<Double>builder()
                .result(predictService.getEasyCorrectRatio(student))
                .build();
    }

    @GetMapping("/hard-questions-attempted")
    public ApiResponse<Integer> getHardQuestionsAttempted(@RequestParam String student) {
        return ApiResponse.<Integer>builder()
                .result(predictService.getHardQuestionsAttempted(student))
                .build();
    }

    @GetMapping("/exam-trend")
    public ApiResponse<Double> getExamTrend(@RequestParam String student) {
        return ApiResponse.<Double>builder()
                .result(predictService.getExamTrend(student))
                .build();
    }

    @GetMapping("/avg-time-per-question")
    public ApiResponse<Double> getAvgTimePerQuestion(@RequestParam String student) {
        return ApiResponse.<Double>builder()
                .result(predictService.getAvgTimePerQuestion(student))
                .build();
    }

    @GetMapping("/consistency")
    public ApiResponse<Double> getConsistency(@RequestParam String student) {
        return ApiResponse.<Double>builder()
                .result(predictService.getConsistency(student))
                .build();
    }

    @GetMapping("/recent-streak")
    public ApiResponse<Integer> getRecentStreak(@RequestParam String student) {
        return ApiResponse.<Integer>builder()
                .result(predictService.getRecentStreak(student))
                .build();
    }
}
