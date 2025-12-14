package com.example.exam_service.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuestionIdsRequest {
    private List<Integer> questionIds;
}
