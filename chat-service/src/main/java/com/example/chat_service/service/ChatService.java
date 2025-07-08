package com.example.chat_service.service;

import com.example.chat_service.dto.request.ChatCreationRequest;
import com.example.chat_service.dto.response.ChatResponse;

import java.util.List;

public interface ChatService {
    ChatResponse createChat(ChatCreationRequest request);
    List<ChatResponse> getChats(Long  conversationId);
}
