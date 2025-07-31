package com.example.post_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentCreationRequest {
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private Long post_id;
}
