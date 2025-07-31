package com.example.post_service.dto.response;

import com.example.post_service.entity.Post;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCommentResponse {
    private Integer commentId;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private Post post;

    private String avatar;
    private String fullName;

}
