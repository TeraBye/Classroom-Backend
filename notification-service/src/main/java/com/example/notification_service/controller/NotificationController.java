package com.example.notification_service.controller;

import com.example.notification_service.dto.request.NotificationRequest;
import com.example.notification_service.dto.response.ApiResponse;
import com.example.notification_service.dto.response.NotificationResponse;
import com.example.notification_service.service.NotificationService;
import com.google.cloud.firestore.WriteResult;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    NotificationService notificationService;

    @GetMapping("/{username}")
    public ApiResponse<List<NotificationResponse>> getNotificationsByUsername
            (@PathVariable String username) throws ExecutionException, InterruptedException {
        return ApiResponse.<List<NotificationResponse>>builder()
                .result(notificationService.getNotificationsByUsername(username))
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<Void> createNotification(@RequestBody NotificationRequest request)
            throws ExecutionException, InterruptedException {
        WriteResult result = notificationService.saveNotification(request);
        return ApiResponse.<Void>builder()
                .message("Notification created at: " + result.getUpdateTime())
                .build();
    }
}
