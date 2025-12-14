package com.example.exam_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PracticeExamCreationRequest {
    private String title;

    private Integer duration; // thời lượng làm bài (phút)

    private Integer numberOfQuestion;

    private LocalDateTime beginTime;

    private String student; // Người tạo là học sinh

    private Integer classId;

    private Integer subjectId;
}

