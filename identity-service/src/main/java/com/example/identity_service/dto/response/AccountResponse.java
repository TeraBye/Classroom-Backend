package com.example.identity_service.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {
    private int userId;

    private String username;

}
