package com.example.notification_service.service.impl;

import com.example.notification_service.dto.request.ListUsernameRequest;
import com.example.notification_service.dto.request.NotificationRequest;
import com.example.notification_service.dto.response.NotificationResponse;
import com.example.notification_service.dto.response.UserProfileResponse;
import com.example.notification_service.entity.Notification;
import com.example.notification_service.repository.httpclient.ProfileClient;
import com.example.notification_service.service.NotificationService;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationServiceImpl implements NotificationService {
    Firestore firestore;
    ProfileClient profileClient;
    @Override
    public WriteResult saveNotification(NotificationRequest request) throws ExecutionException, InterruptedException {
        Map<String, Object> notificationFB = new HashMap<>();
        notificationFB.put("content", request.getContent());
        notificationFB.put("senderUsername", request.getSenderUsername());
        notificationFB.put("receiverUsername", request.getReceiverUsername());
        notificationFB.put("isRead", request.isRead());
        notificationFB.put("timestamp", Timestamp.now());

        DocumentReference docRef = firestore.collection("notification").document();
        ApiFuture<WriteResult> writeResult = docRef.set(notificationFB);
        return writeResult.get();
    }

    @Override
    public List<NotificationResponse> getNotificationsByUsername(String username) throws ExecutionException, InterruptedException {
//        Get notifications
        Firestore db = FirestoreClient.getFirestore();
        List<Notification> notifications = new ArrayList<>();

        // Query lấy notification theo receiverUsername
        Query query = db.collection("notification")
                .whereEqualTo("receiverUsername", username)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(5); // Lấy tối đa 5 thông báo mới nhất

        QuerySnapshot querySnapshot = query.get().get();

        for (QueryDocumentSnapshot document : querySnapshot) {
            Notification notification = document.toObject(Notification.class);
            notifications.add(notification);
        }

//        Get avatar from profile and return dto

//        Get sender usernames
        Set<String> usernames = notifications.stream()
                .map(Notification::getSenderUsername)
                .collect(Collectors.toSet());

        List<UserProfileResponse> profileResponses = profileClient.getListUserByListUsername(
                new ListUsernameRequest(new ArrayList<>(usernames)))
                .getResult();

        Map<String, UserProfileResponse> profileResponseMap = profileResponses.stream()
                .collect(Collectors.toMap(
                        UserProfileResponse::getUsername,
                        profile -> profile,
                        (existing, replacement) -> existing
                ));

        List<NotificationResponse> notificationResponses = notifications.stream()
                .map(notification -> {
                    UserProfileResponse profile = profileResponseMap.get(notification.getSenderUsername());
                    return NotificationResponse.builder()
                            .content(notification.getContent())
                            .timestamp(notification.getTimestamp())
                            .senderUsername(notification.getSenderUsername())
                            .avatar(profile.getAvatar())
                            .build();
                }).collect(Collectors.toList());
        return notificationResponses;
    }
}
