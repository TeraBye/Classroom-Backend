package com.example.exam_service.dto.request;

import com.example.exam_service.entity.Exam;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamSubmissionRequest {
    private String student;
    private Long examId;

    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;

    private Float score;
}