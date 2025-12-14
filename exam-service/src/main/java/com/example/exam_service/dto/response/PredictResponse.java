package com.example.exam_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PredictResponse {
    private float avgRecentScore;
    private float hardCorrectRatio;
    private float mediumCorrectRatio;
    private float easyCorrectRatio;
    private int hardQuestionsAttempted;
    private float examTrend;
    private float avgTimePerQuestion;
    private float consistency;
    private int recentStreak;
}
