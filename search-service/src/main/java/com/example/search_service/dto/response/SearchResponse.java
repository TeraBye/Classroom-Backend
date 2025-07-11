package com.example.search_service.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchResponse {
    Page<UserProfileResponse> users;
    Page<ClassroomResponse> classrooms;
}
