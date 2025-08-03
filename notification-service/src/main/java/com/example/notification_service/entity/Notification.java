package com.example.notification_service.entity;

import com.google.cloud.Timestamp;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class Notification {
    String receiverUsername;
    String content;
    Timestamp timestamp;
    String senderUsername;
    boolean isRead = false;
}
