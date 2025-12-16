package com.example.assignment_service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignmentCreateRequest {
    String fileUrl;
    @NotNull(message = "Deadline is required")
    @JsonFormat(pattern = "HH:mm:ss dd:MM:yyyy")
    String deadline;
    @NotBlank(message = "Name is required")
    String name;
    @NotBlank(message = "Username is required")
    String username;
    @NotNull(message = "Classroom ID is required")
    Integer classroomId;
    @JsonFormat(pattern = "HH:mm:ss dd:MM:yyyy")
    LocalDateTime startAt;
}
