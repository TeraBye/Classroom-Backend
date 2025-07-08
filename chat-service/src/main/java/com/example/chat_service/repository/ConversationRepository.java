package com.example.chat_service.repository;

import com.example.chat_service.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findBySenderUsernameAndReceiverUsername(String senderUsername, String receiverUsername);

}
