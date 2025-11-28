package com.example.classroom_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JoinRequestResponse {
    Integer id;
    Integer classroomId;
    String classroomName;
    String username;
    String fullName;
    String avatar;
    String status;
    LocalDateTime requestedAt;
    LocalDateTime approvedAt;
}
