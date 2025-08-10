package com.example.profile_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

    private int userId;

    @NotBlank
    private String username;

    @NotBlank
    private String fullName;

    @Email
    private String email;

    @Past
    private LocalDate dob;

    @NotBlank
    private String roles;
}
