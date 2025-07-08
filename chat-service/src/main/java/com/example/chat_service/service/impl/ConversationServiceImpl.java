package com.example.chat_service.service.impl;

import com.example.chat_service.dto.request.ConversationCreationRequest;
import com.example.chat_service.dto.response.ConversationResponse;
import com.example.chat_service.entity.Conversation;
import com.example.chat_service.mapper.ConversationMapper;
import com.example.chat_service.repository.ConversationRepository;
import com.example.chat_service.service.ConversationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationServiceImpl implements ConversationService {
    ConversationRepository conversationRepository;

    ConversationMapper conversationMapper;

    @Override
    public ConversationResponse createConversation(ConversationCreationRequest request){
        Conversation conversation = conversationMapper.toConversation(request);
        return conversationMapper.toConversationResponse(
                conversationRepository.save(conversation));
    }

}
