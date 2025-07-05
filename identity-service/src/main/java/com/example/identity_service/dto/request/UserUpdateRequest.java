package com.example.identity_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.example.identity_service.validator.DobConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    @Email
    private String email;

    @Size(min = 6, message = "INVALID_PASSWORD")
    private String password;

    @NotBlank
    private String fullName;

    @DobConstraint(min = 16, message = "INVALID_DOB")
    private String dob;
}
