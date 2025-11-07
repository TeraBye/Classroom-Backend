package com.example.assignment_service.dto.response;

import com.example.assignment_service.enums.AssignmentSubmitStatus;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class StudentAssignmentViewResponse {
    // This is used by teacher view
    private String studentUsername;
    private String studentName; // lấy từ ProfileClient
    private String avatar;
    private AssignmentSubmitStatus status; // "NOT_SUBMITTED", "SUBMITTED", "LATE"
    private AssignmentDetailResponse submission;
}
