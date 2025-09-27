package com.example.question_service.dto.request;

import com.example.question_service.enums.Level;
import com.example.question_service.validator.DistinctOptions;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@DistinctOptions
public class QuestionCreateRequest {
     @NotNull(message = "Content is required")
     String content;
     @NotNull(message = "Option A is required")
     String optionA;
     @NotNull(message = "option B is required")
     String optionB;
     @NotNull(message = "option C is required")
     String optionC;
     @NotNull(message = "option D is required")
     String optionD;
     @NotNull(message = "Correct answer is required")
     String correctAnswer;
     @NotNull(message = "Explanation is required")
     String explanation;
     @NotNull(message = "Level is required")
     @Enumerated(EnumType.STRING)
     Level level;
     @NotNull(message = "Username is required")
     String username;
     @NotNull(message = "SubjectId is required")
     Integer subjectId;

}
