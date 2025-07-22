package com.example.assignment_service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignmentCreateRequest {
    @NotNull(message = "File is required")
    MultipartFile file;

//    @JsonFormat(pattern = "HH:mm:ss dd:MM:yyyy")
//    LocalDateTime deadline;
    String deadline;

    String name;

    String username;

    Integer classroomId;
}
