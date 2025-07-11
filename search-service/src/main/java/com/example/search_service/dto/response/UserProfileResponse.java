package com.example.search_service.dto.response;


import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileResponse {
    private String id;

    private int userId;

    private String username;

    private String fullName;

    private String email;

    private LocalDate dob;

    private String avatar;
}
