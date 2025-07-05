package com.example.identity_service.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.example.identity_service.validator.DobConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDTO {
    @Size(min = 5, message = "INVALID_USERNAME")
    private String username;

    @Email
    private String email;

    @Size(min = 6, message = "INVALID_PASSWORD")
    private String password;

    @NotBlank
    private String fullName;

    @DobConstraint(min = 16, message = "INVALID_DOB")
    private LocalDate dob;
}
