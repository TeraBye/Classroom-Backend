package com.example.assignment_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignmentResponse {
    Integer id;
    String fileUrl;

    @JsonFormat(pattern = "HH:mm:ss dd:MM:yyyy")
    LocalDateTime deadline;

    String name;

    String assignmentCode;

    String username;

    Integer classroomId;
}
