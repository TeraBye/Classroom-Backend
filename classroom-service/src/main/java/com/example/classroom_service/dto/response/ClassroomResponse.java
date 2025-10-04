package com.example.classroom_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    String meetLink;

    boolean isPublic;

    String classCode;

    @Builder.Default
    Long postNum = 0L;

    int studentNum = 0;

    boolean isDeleted;

    String teacherUsername;

    String teacherName;

    Integer subjectId;

    String subjectName;

    LocalDateTime createdAt;
}
