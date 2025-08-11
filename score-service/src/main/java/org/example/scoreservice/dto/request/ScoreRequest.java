package org.example.scoreservice.dto.request;

import lombok.Builder;
import lombok.Data;
import org.example.scoreservice.enums.TYPEOFSCORE;

@Data
@Builder
public class ScoreRequest {
    int scoreDetailId;

    Float score;

    int studentId;

    Integer classroomId;

    TYPEOFSCORE typeofscore;

}
