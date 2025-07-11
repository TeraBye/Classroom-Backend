package com.example.classroom_service.dto.response;

import com.example.classroom_service.entity.Classroom;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentResponse {
     Integer id;

     Classroom classroom;

     String studentUsername;

     LocalDateTime joinedAt;
}
