package com.example.exam_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredictRequest {

    private double avgRecentScore;
    private double hardCorrectRatio;
    private double mediumCorrectRatio;
    private double easyCorrectRatio;

    private int hardQuestionsAttempted;

    private double examTrend;
    private double avgTimePerQuestion;
    private double consistency;

    private int recentStreak;
}

