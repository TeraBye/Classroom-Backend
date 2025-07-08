package com.example.chat_service.mapper;

import com.example.chat_service.dto.request.ConversationCreationRequest;
import com.example.chat_service.dto.response.ConversationResponse;
import com.example.chat_service.entity.Conversation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConversationMapper {
    Conversation toConversation(ConversationCreationRequest request);

    ConversationResponse toConversationResponse(Conversation conversation);

    List<Conversation> toConversations(List<ConversationCreationRequest> requests);
}
