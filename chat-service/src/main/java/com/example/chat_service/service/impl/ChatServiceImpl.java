package com.example.chat_service.service.impl;

import com.example.chat_service.dto.request.ChatCreationRequest;
import com.example.chat_service.dto.response.ChatResponse;
import com.example.chat_service.entity.Chat;
import com.example.chat_service.entity.Conversation;
import com.example.chat_service.mapper.ChatMapper;
import com.example.chat_service.repository.ChatRepository;
import com.example.chat_service.repository.ConversationRepository;
import com.example.chat_service.service.ChatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChatServiceImpl implements ChatService {
    ChatRepository chatRepository;
    ConversationRepository conversationRepository;
    ChatMapper chatMapper;
    SimpMessagingTemplate messagingTemplate;

    @Override
    public ChatResponse createChat(ChatCreationRequest request){
        Conversation conversation = conversationRepository.findById(request.getConversationId()).
                orElseThrow(() -> new RuntimeException("conversation not found"));
        Chat chat = chatMapper.toChat(request);
        chat.setConversation(conversation);

        ChatResponse chatResponse = chatMapper.toChatResponse(chatRepository.save(chat));

        messagingTemplate.convertAndSend(
                "/topic/conversations/" + conversation.getId(),
                chatResponse
        );

        return chatResponse;

    }

    @Override
    public List<ChatResponse> getChats(Long conversationId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return chatMapper.toChatResponseList(
                chatRepository.findByConversation_IdOrderByTimeDesc(conversationId, pageable)
        );
    }

}
