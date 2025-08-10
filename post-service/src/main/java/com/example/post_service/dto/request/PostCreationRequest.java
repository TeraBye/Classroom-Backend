package com.example.post_service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostCreationRequest {
    private String title;
    private String content;
    private String username;
    @JsonFormat(pattern = "HH:mm:ss dd:MM:yyyy")
    private LocalDateTime deadline;
    private String fileUrl;
    private Integer classId;
}
