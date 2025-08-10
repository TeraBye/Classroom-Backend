package com.example.assignment_service.dto.response;

import com.example.assignment_service.entity.Assignment;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentDetailResponse {
    Integer id;
    Assignment assignment;
    @JsonFormat(pattern = "HH:mm:ss dd:MM:yyyy")
    LocalDateTime submitTime;

    String note;

    String fileUrl;

    String studentUsername;

}
