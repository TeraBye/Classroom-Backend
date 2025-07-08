package com.example.profile_service.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ListUsernameRequest {
    private List<String> listUsername;
}
