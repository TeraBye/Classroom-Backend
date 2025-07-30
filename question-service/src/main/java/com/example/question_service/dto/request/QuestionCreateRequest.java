package com.example.question_service.dto.request;

import com.example.question_service.enums.Level;
import com.example.question_service.validator.DistinctOptions;
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
@DistinctOptions
public class QuestionCreateRequest {
     String content;

     String optionA;

     String optionB;

     String optionC;

     String optionD;

     String correctAnswer;

     String explanation;

     @Enumerated(EnumType.STRING)
     Level level;

     String username;

     Integer subjectId;

}
