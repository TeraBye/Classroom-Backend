package com.example.chat_service.mapper;

import com.example.chat_service.dto.request.ConversationCreationRequest;
import com.example.chat_service.dto.response.ConversationResponse;
import com.example.chat_service.dto.response.UserConversationResponse;
import com.example.chat_service.dto.response.UserProfileResponse;
import com.example.chat_service.entity.Conversation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConversationMapper {
    Conversation toConversation(ConversationCreationRequest request);

    ConversationResponse toConversationResponse(Conversation conversation);

    UserConversationResponse toUserConversationResponse(
            ConversationCreationRequest request1, UserProfileResponse request2);

    @Mapping(source = "request1.id", target = "conver_id")
    UserConversationResponse toUserConversationResponse2(
            Conversation request1,
            UserProfileResponse request2);
}
