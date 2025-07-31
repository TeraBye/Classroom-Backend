package com.example.exam_service.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamCreationRequest {
    private String title;

    private LocalDateTime createdAt = LocalDateTime.now();

    private Integer duration; // thời lượng làm bài (phút)

    private Integer numberOfQuestion;

    private LocalDateTime beginTime;

    private String teacher;

    private String classId;

    private String subjectId;
}
