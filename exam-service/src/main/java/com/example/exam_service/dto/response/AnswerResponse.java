package com.example.exam_service.dto.response;

import com.example.exam_service.entity.ExamSubmission;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerResponse {
    // dưới này của answer
    private Long id;
    private int questionId;
    private String selectedOption;

    //Dưới này của question
    private String content;

    private String optionA;

    private String optionB;

    private String optionC;

    private String optionD;

    private String correctAnswer;
}
