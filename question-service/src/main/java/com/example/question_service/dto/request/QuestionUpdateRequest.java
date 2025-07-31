package com.example.question_service.dto.request;

import com.example.question_service.enums.Level;
import com.example.question_service.validator.DistinctOptions;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@DistinctOptions
public class QuestionUpdateRequest {
     String content;

     String optionA;

     String optionB;

     String optionC;

     String optionD;

     String correctAnswer;

     String explanation;

     Level level;

     String username;

     Integer subjectId;

}
