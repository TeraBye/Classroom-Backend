package com.example.identity_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileClientResponse {
    private String id;

    private int userId;

    private String username;

    private String fullName;

    private String email;

    private LocalDate dob;

    private String avatar;
}