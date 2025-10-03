package com.example.classroom_service.dto.response;

import com.example.classroom_service.entity.Subject;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassroomResponse {
    Integer id;

    String name;

    Subject subject;

    String meetLink;

    Boolean isPublic;

    String classCode;

    LocalDateTime createdAt;
}
