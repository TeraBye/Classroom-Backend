package com.example.post_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPostResponse {
    private Long postId;
    private Integer assignmentId;
    private String avatar;
    private String fullName;
    private LocalDateTime createdAt;
    private String title;
    private LocalDateTime deadline;
    private String content;
    private String fileUrl;
    private String username;
}
