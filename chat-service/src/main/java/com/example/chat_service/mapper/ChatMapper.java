package com.example.chat_service.mapper;

import com.example.chat_service.dto.request.ChatCreationRequest;
import com.example.chat_service.dto.response.ChatResponse;
import com.example.chat_service.entity.Chat;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    Chat toChat(ChatCreationRequest request);
    ChatResponse toChatResponse(Chat chat);
    List<ChatResponse> toChatResponseList(List<Chat> chats);
}
