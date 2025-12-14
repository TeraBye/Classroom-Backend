package com.example.exam_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredictRequest {

    @JsonProperty("avg_recent_score")
    private double avgRecentScore;

    @JsonProperty("hard_correct_ratio")
    private double hardCorrectRatio;

    @JsonProperty("medium_correct_ratio")
    private double mediumCorrectRatio;

    @JsonProperty("easy_correct_ratio")
    private double easyCorrectRatio;

    @JsonProperty("hard_questions_attempted")
    private int hardQuestionsAttempted;

    @JsonProperty("exam_trend")
    private double examTrend;

    @JsonProperty("avg_time_per_question")
    private double avgTimePerQuestion;

    @JsonProperty("consistency")
    private double consistency;

    @JsonProperty("recent_streak")
    private int recentStreak;
}

