package com.example.classroom_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassroomUpdateRequest {
    String name;

    String meetLink;

    boolean isPublic;

}
