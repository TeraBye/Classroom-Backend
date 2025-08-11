package org.example.scoreservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ClassroomResponse {
    Integer classroomId;
    int total;
    Integer subjectId;
    String subjectName;
}
