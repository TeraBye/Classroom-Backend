package com.example.exam_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAnswerRequest {
    private Long examSubmissionId;
    private int questionId;
    private String selectedOption;
    private Integer exam_time;
}
