package com.example.exam_service.service;

import com.example.exam_service.dto.request.PredictRequest;
import com.example.exam_service.dto.response.PredictResponse;
import com.example.exam_service.dto.response.ProficiencyPredictionResponse;

public interface PredictService {
    Double getAvgRecentScores(String student);
    Double getHardCorrectRatio(String student);
    Double getMediumCorrectRatio(String student);
    Double getEasyCorrectRatio(String student);
    Integer getHardQuestionsAttempted(String student);
    Double getExamTrend(String student);
    Double getAvgTimePerQuestion(String student);
    Double getConsistency(String student);
    Integer getRecentStreak(String student);
    PredictResponse getPredictData(String student);
    PredictRequest getPredictRequestPayload(String student);
    ProficiencyPredictionResponse getProficiencyPrediction(String student);
}
