package org.example.scoreservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectWithClassroomResponse {
    Integer classroomId;

    Integer id;

    String name;
}
