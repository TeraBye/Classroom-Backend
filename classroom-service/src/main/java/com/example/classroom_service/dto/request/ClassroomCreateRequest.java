package com.example.classroom_service.dto.request;

import com.example.classroom_service.entity.Subject;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassroomCreateRequest {
    String name;

    Integer subjectId;

    String meetLink;

    Boolean isPublic;

    String teacherUsername;

}
