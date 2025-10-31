package com.example.assignment_service.dto.response;

import com.example.assignment_service.enums.AssignmentDetailStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignmentDetailResponse {
    Integer id;
    Integer assignmentId;
    @JsonFormat(pattern = "HH:mm:ss dd:MM:yyyy")
    LocalDateTime submitTime;

    String note;

    String fileUrl;

//    String studentUsername;
//
//    String studentName;
//
//    String avatar;

    AssignmentDetailStatus status;
    Integer submissionCount;

}
