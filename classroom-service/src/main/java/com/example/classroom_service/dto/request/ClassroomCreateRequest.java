package com.example.classroom_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

    @JsonProperty("public")
    Boolean isPublic;

    String teacherUsername;

}
