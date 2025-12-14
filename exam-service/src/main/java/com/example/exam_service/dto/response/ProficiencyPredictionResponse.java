package com.example.exam_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProficiencyPredictionResponse {
    @JsonProperty("proficiency_pred")
    private Double proficiencyPred;

    @JsonProperty("easy_ratio")
    private Double easyRatio;

    @JsonProperty("medium_ratio")
    private Double mediumRatio;

    @JsonProperty("hard_ratio")
    private Double hardRatio;
}

