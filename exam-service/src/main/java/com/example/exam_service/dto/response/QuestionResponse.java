package com.example.exam_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionResponse {
    Integer id;
    String content;
    String optionA;
    String optionB;
    String optionC;
    String optionD;
    String correctAnswer;
    String explanation;
    String level; // Using String instead of Level enum for simplicity
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String username;
    Integer subjectId;
}
