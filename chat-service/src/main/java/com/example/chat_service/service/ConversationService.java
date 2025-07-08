package com.example.chat_service.service;

import com.example.chat_service.dto.request.ConversationCreationRequest;
import com.example.chat_service.dto.response.ConversationResponse;

public interface ConversationService {
    ConversationResponse createConversation(ConversationCreationRequest request);
}
