package com.example.chat_service.controller;

import com.example.chat_service.dto.request.ConversationCreationRequest;
import com.example.chat_service.dto.response.ApiResponse;
import com.example.chat_service.dto.response.ConversationResponse;
import com.example.chat_service.dto.response.UserConversationResponse;
import com.example.chat_service.entity.Conversation;
import com.example.chat_service.service.ConversationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversation")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationController {
    ConversationService conversationService;

    @PostMapping
    public ApiResponse<UserConversationResponse> createConversation(
            @RequestBody ConversationCreationRequest request) {
        return ApiResponse.<UserConversationResponse>builder()
                .result(conversationService.createConversation(request))
                .build();
    }

    @GetMapping("/{username}")
    public ApiResponse<List<UserConversationResponse>> getConversations(
            @PathVariable("username") String username
    ){
        return ApiResponse.<List<UserConversationResponse>>builder()
                .result(conversationService.getConversations(username))
                .build();
    }
}
