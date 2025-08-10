package com.example.exam_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinalStudentExamViewResponse {
    ExamSubmissionResponse examSubmission;
    List<AnswerResponse> answers;
}
