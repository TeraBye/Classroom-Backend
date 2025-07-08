package com.example.chat_service.controller;

import com.example.chat_service.dto.request.ConversationCreationRequest;
import com.example.chat_service.dto.response.ApiResponse;
import com.example.chat_service.dto.response.ConversationResponse;
import com.example.chat_service.entity.Conversation;
import com.example.chat_service.service.ConversationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conversation")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationController {
    ConversationService conversationService;

    @PostMapping
    public ApiResponse<ConversationResponse> createConversation(
            @RequestBody ConversationCreationRequest request) {
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationService.createConversation(request))
                .build();
    }
}
