package com.example.logging_service.consumer;

import com.example.logging_service.entity.AuditLog;
import com.example.logging_service.event.AuditLogEvent;
import com.example.logging_service.repository.AuditLogRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@KafkaListener(topics = "audit.log",
        groupId = "logging-service",
        containerFactory = "kafkaListenerContainerFactory")
public class LoggingConsumer {
    AuditLogRepository auditLogRepository;

    @KafkaHandler
    public void consumeAuditLog(AuditLogEvent event) {
        log.info("Received auditLog event {}", event);
        AuditLog log = AuditLog.builder()
                .username(event.getUsername())
                .role(event.getRole())
                .action(event.getAction())
                .description(event.getDescription())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        auditLogRepository.save(log);  // Giả sử có AuditLogRepository
    }
}
