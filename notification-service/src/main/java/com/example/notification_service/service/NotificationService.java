package com.example.notification_service.service;

import com.example.notification_service.dto.request.NotificationRequest;
import com.example.notification_service.dto.response.NotificationResponse;
import com.example.notification_service.entity.Notification;
import com.google.cloud.firestore.WriteResult;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface NotificationService {
    WriteResult saveNotification(NotificationRequest request) throws ExecutionException, InterruptedException;
    List<NotificationResponse> getNotificationsByUsername(String username) throws ExecutionException, InterruptedException;
    void markAllAsRead(String username);
}
