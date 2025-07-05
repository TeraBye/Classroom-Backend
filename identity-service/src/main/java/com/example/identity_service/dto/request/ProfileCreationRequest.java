package com.example.identity_service.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileCreationRequest {
    private int userId;

    private String username;

    private String fullName;

    private String email;

    private LocalDate dob;
}
