package org.example.scoreservice.dto.request;

import lombok.Builder;
import lombok.Data;
import org.example.scoreservice.enums.TYPEOFSCORE;

@Data
@Builder
public class ScoreRequest {
    int scoreDetailId;

    Float score;

    String studentId;

    String classroomId;

    TYPEOFSCORE typeofscore;

}
