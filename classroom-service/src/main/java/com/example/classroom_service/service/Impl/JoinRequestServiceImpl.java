package com.example.classroom_service.service.Impl;

import com.example.classroom_service.dto.request.SearchJoinRequest;
import com.example.classroom_service.dto.request.UpdateJoinRequest;
import com.example.classroom_service.dto.response.JoinRequestResponse;
import com.example.classroom_service.dto.response.UserProfileResponse;
import com.example.classroom_service.entity.ClassroomDetail;
import com.example.classroom_service.entity.JoinRequest;
import com.example.classroom_service.enums.JoinStatus;
import com.example.classroom_service.mapper.JoinRequestMapper;
import com.example.classroom_service.repository.ClassroomDetailRepository;
import com.example.classroom_service.repository.JoinRequestRepository;
import com.example.classroom_service.repository.httpclient.ProfileClient;
import com.example.classroom_service.service.JoinRequestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class JoinRequestServiceImpl implements JoinRequestService {
    JoinRequestRepository joinRequestRepository;
    ProfileClient profileClient;
    KafkaTemplate<String, Object> kafkaTemplate;
    ClassroomDetailRepository classroomDetailRepository;

    @Override
    public Page<JoinRequestResponse> getRequests(SearchJoinRequest request, Pageable pageable) {
        return joinRequestRepository
                .findByTeacherUsernameAndStatus(request.getUsername(), request.getStatus(), pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional
    public JoinRequestResponse updateStatus(Integer requestId, UpdateJoinRequest request) {
        JoinRequest joinRequest = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!joinRequest.getTeacherUsername().equals(request.getUsername())) {
            throw new RuntimeException("Do not have permission");
        }

        if (!JoinStatus.PENDING.name().equals(joinRequest.getStatus())) {
            throw new RuntimeException("Join request already processed");
        }

        joinRequest.setStatus(request.getStatus());
        joinRequest.setApprovedAt(LocalDateTime.now());
        joinRequest = joinRequestRepository.save(joinRequest);

        if (JoinStatus.APPROVED.name().equals(request.getStatus())) {
            // Tự động thêm học sinh vào lớp
            ClassroomDetail detail = ClassroomDetail.builder()
                    .classroom(joinRequest.getClassroom())
                    .studentUsername(joinRequest.getStudentUsername())
                    .joinedAt(LocalDateTime.now())
                    .build();
            classroomDetailRepository.save(detail);

            // Gửi noti cho học sinh: "Đã được duyệt"
            sendNotification(joinRequest.getStudentUsername(), "join_approved", "Join request has been approved!");
        } else {
            // Gửi noti: "Bị từ chối"
            sendNotification(joinRequest.getStudentUsername(), "join_rejected", "Join request has been rejected.");
        }

        // Gửi noti cho giáo viên: "Đã xử lý xong"
        sendNotification(request.getUsername(), "join_request_processed", "Đã xử lý yêu cầu tham gia từ " + joinRequest.getStudentUsername());

        return mapToResponse(joinRequest);
    }

    private JoinRequestResponse mapToResponse(JoinRequest r) {
        UserProfileResponse profile = profileClient
                .getUserProfileByUsername(r.getStudentUsername())
                .getResult();

        return new JoinRequestResponse(
                r.getId(),
                r.getClassroom().getId(),
                r.getClassroom().getName(),
                r.getStudentUsername(),
                profile.getFullName(),
                profile.getAvatar(),
                r.getStatus(),
                r.getRequestedAt(),
                r.getApprovedAt()
        );
    }

    private void sendNotification(String receiver, String type, String content) {
        Map<String, Object> msg = Map.of(
                "type", type,
                "content", content,
                "senderUsername", "SYSTEM",
                "receiverUsername", receiver
        );
        kafkaTemplate.send("notifications", msg);
    }
}
