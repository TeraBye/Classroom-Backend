package com.example.notification_service.dto.response;

import com.google.cloud.Timestamp;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse {
    String receiverUsername;
    String senderUsername;
    String content;
    String type; // POST hoặc COMMENT
    Timestamp timestamp;
    boolean isRead;
    String avatar;
}
