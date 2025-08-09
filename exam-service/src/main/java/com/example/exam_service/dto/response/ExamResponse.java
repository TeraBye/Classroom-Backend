package com.example.exam_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamResponse {
    private Long id;

    private String title;

    private LocalDateTime createdAt = LocalDateTime.now();

    private Integer duration; // thời lượng làm bài (phút)

    private Integer numberOfQuestion;

    private LocalDateTime beginTime;

    private String teacher;

    private int classId;

    private int subjectId;
}
