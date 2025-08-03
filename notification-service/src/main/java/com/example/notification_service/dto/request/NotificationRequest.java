package com.example.notification_service.dto.request;

import com.google.cloud.Timestamp;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class NotificationRequest {
    String receiverUsername;
    String senderUsername;
    String content;
    String type; // POST hoặc COMMENT
    Timestamp timestamp;
    boolean isRead = false;
}
