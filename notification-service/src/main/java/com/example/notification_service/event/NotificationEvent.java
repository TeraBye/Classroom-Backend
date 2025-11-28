package com.example.notification_service.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private String type;
    private String content;
    private String senderUsername;
    private String classroomId;
//    private String mentionedUsername; // cho tương lai
}
