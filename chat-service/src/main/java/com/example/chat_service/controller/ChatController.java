package com.example.chat_service.controller;

import com.example.chat_service.dto.request.ChatCreationRequest;
import com.example.chat_service.dto.response.ApiResponse;
import com.example.chat_service.dto.response.ChatResponse;
import com.example.chat_service.service.ChatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/startChat")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {
    ChatService chatService;

    @PostMapping
    public ApiResponse<ChatResponse> sendChat(
            @RequestBody ChatCreationRequest request) {
        return ApiResponse.<ChatResponse>builder()
                .result(chatService.createChat(request))
                .build();
    }

    @GetMapping("/{conversationId}")
    public ApiResponse<List<ChatResponse>> getChat(
            @PathVariable Long conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<ChatResponse>>builder()
                .result(chatService.getChats(conversationId, page, size))
                .build();
    }

}
