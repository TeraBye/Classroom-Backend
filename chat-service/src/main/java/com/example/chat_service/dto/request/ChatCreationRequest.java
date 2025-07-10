package com.example.chat_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatCreationRequest {
    private String content;
    private LocalDateTime time;
    private Long conversationId;
    private String sender;
}