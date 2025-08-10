package com.example.question_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QuestionPagingResponse<T> {
    private List<T> items;
    private int nextCursor;
    private boolean hasNext;
}
