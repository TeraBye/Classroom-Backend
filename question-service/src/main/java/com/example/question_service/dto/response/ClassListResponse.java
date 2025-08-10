package com.example.question_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassListResponse {
    Integer subjectId;
    String subjectName;
    int total;
}
