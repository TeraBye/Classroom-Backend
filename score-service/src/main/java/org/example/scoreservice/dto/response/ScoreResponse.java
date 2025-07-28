package org.example.scoreservice.dto.response;

import lombok.Builder;
import lombok.Data;
import org.example.scoreservice.enums.TYPEOFSCORE;

@Data
@Builder
public class ScoreResponse {
    int scoreDetailId;

    Float score;

    String studentId;

    String classroomId;

    TYPEOFSCORE typeofscore;
}
