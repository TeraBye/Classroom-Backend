package com.example.classroom_service.dto.request;

import com.example.classroom_service.entity.Subject;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectCreateRequest {
    String code;
    String name;
}
