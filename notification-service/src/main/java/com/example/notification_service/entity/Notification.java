package com.example.notification_service.entity;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.PropertyName;
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
    @Setter(AccessLevel.NONE)
    boolean isRead = false;

    @PropertyName("isRead")
    public boolean getIsRead() {
        return isRead;
    }

    @PropertyName("isRead")
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
}
