package com.example.assignment_service.dto.request;

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
public class AssignmentUpdateRequest {
    String fileUrl;

    @JsonFormat(pattern = "HH:mm:ss dd:MM:yyyy")
    LocalDateTime deadline;

    String name;

    String username;
}
