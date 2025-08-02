package com.example.exam_service.dto.response;

import com.example.exam_service.entity.ExamSubmission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamSubmissionViewResponse {
    ExamSubmission examSubmission;
    List<QuestionResponse> questionResponses;
}
