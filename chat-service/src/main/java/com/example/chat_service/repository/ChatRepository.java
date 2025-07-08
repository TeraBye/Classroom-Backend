package com.example.chat_service.repository;

import com.example.chat_service.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByConversation_Id(Long conversationId);
}
