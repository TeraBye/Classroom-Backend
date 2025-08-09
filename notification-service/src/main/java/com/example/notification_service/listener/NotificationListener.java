package com.example.notification_service.listener;

import com.example.notification_service.dto.request.NotificationRequest;
import com.example.notification_service.entity.Notification;
import com.example.notification_service.repository.httpclient.ClassroomClient;
import com.example.notification_service.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.Timestamp;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationListener {
    SimpMessagingTemplate messagingTemplate;
    ClassroomClient classroomClient;
    NotificationService notificationService;
    @KafkaListener(topics = "notifications", groupId = "notification-group")
    public void handleNotification(String message) throws JsonProcessingException, ExecutionException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = mapper.readValue(message, new TypeReference<>() {});

        String content = data.get("content");
        String type = data.get("type");
        String senderUsername = data.get("senderUsername");
        String classroomId = data.get("classroomId");
        List<String> allUsernames = new ArrayList<>();

        if (Objects.equals(type, "post")) {
            allUsernames = classroomClient.findStudentUsernamesByClassroomId(Integer.parseInt(classroomId)).getResult();
        }

        for (String username : allUsernames) {
            messagingTemplate.convertAndSend("/topic/notifications" + username, senderUsername);
        }

        if (!allUsernames.isEmpty()) {
            for (String username : allUsernames) {
                NotificationRequest notification = NotificationRequest.builder()
                        .senderUsername(senderUsername)
                        .receiverUsername(username)
                        .content(content)
                        .timestamp(Timestamp.now())
                        .isRead(false)
                        .build();
                notificationService.saveNotification(notification);
            }
        }
    }
}
