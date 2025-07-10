package com.example.chat_service.service;

import com.example.chat_service.dto.request.ConversationCreationRequest;
import com.example.chat_service.dto.response.ConversationResponse;
import com.example.chat_service.dto.response.UserConversationResponse;

import java.util.List;

public interface ConversationService {
    UserConversationResponse createConversation(ConversationCreationRequest request);
    List<UserConversationResponse> getConversations(String username);
}
