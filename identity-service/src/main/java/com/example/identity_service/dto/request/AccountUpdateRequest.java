package com.example.identity_service.dto.request;

import com.example.identity_service.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class AccountUpdateRequest {
    int userId;
    String username;
    String roles;
}
