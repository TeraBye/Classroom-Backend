package com.example.question_service.dto.request;

import com.example.question_service.enums.Level;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionSearchRequest {
    Integer subjectId;
    int cursor;
    Level level;
    String keyword;
}
