package com.example.exam_service.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ListUsernameRequest {
    private List<String> listUsername;
}

