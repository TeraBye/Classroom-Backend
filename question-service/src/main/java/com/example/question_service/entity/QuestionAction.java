package com.example.question_service.entity;

import com.example.question_service.enums.ActionType;
import com.example.question_service.enums.Level;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionAction {
    ActionType type;
    Question before;
    Question after;
}
