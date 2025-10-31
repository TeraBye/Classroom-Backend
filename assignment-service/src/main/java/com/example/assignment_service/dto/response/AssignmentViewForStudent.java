package com.example.assignment_service.dto.response;

import com.example.assignment_service.enums.AssignmentSubmitStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignmentViewForStudent {
    AssignmentResponse assignment;
    AssignmentSubmitStatus status;
    AssignmentDetailResponse submission;
}
