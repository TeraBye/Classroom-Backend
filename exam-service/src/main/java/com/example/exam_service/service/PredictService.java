package com.example.exam_service.service;

import com.example.exam_service.dto.response.PredictResponse;

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
}
