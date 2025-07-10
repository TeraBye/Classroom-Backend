package com.example.chat_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserConversationResponse {
    private Long conver_id;
    private String senderUsername;
    private String receiverUsername;
    private String fullName;
    private String avatar;
}
