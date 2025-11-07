package com.example.assignment_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignmentSubmitRequest {
    @NotNull(message = "Assignment ID is required")
    Integer assignmentId;

    @NotNull(message = "Student username is required")
    String studentUsername;
    String note;
    String fileUrl;

}
