package com.example.chat_service.repository;

import com.example.chat_service.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByConversation_IdOrderByTimeDesc(Long conversationId, Pageable pageable);
}
