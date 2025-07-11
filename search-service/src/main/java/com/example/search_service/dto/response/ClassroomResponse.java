package com.example.search_service.dto.response;


import com.example.search_service.entity.Subject;
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

    String teacherUsername;

    String classCode;

    LocalDateTime createdAt;
}
