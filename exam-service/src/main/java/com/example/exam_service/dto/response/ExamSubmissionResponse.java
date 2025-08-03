package com.example.exam_service.dto.response;

import com.example.exam_service.entity.Exam;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamSubmissionResponse {
    private Long id;

    private String student;

    private LocalDateTime startedAt;

    private LocalDateTime submittedAt;

    private Float score;

    private Integer numberOfCorrectAnswers;
}
