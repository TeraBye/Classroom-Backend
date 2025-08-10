package com.example.post_service.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    private Long postId;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createAt;
    private String assignmentId;
    private Integer classId;
}
