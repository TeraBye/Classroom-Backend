package com.example.question_service.dto.response;

import com.example.question_service.enums.Level;
import jakarta.persistence.*;
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

    Level level;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    String username;

    Integer subjectId;

}
