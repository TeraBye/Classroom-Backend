package com.example.profile_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileCreationRequest {

    private int userId;

    private String username;

    private String fullName;

    private String email;

    private LocalDate dob;
}
