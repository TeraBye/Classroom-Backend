package com.example.chat_service.dto.response;

import com.example.chat_service.entity.Conversation;
import jakarta.persistence.Column;
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
public class ChatResponse {
    private Long id;
    private Conversation conversation;
    private String content;
    private LocalDateTime time;
    private Boolean isRead = false;
    private String sender;
}
