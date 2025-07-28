package com.example.classroom_service.dto.response;

import com.example.classroom_service.entity.Subject;

import java.time.LocalDateTime;

public class GeneralResponse {
    String className;

    Subject subject;

    String meetLink;

    Boolean isPublic;

    String teacherUsername;

    String classCode;

    LocalDateTime createdAt;
}
