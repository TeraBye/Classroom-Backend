package com.example.chat_service.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "conversations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_username", nullable = false)
    private String senderUsername;

    @Column(name = "receiver_username", nullable = false)
    private String receiverUsername;
}

