package com.example.classroom_service.mapper;

import com.example.classroom_service.dto.response.JoinRequestResponse;
import com.example.classroom_service.dto.response.UserProfileResponse;
import com.example.classroom_service.entity.JoinRequest;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface JoinRequestMapper {
    @Mapping(target = "id", source = "joinRequest.id") // Chỉ định rõ nguồn cho id
    @Mapping(target = "classroomId", source = "joinRequest.classroom.id")
    @Mapping(target = "classroomName", source = "joinRequest.classroom.name")
    @Mapping(target = "fullName", source = "profile.fullName")
    @Mapping(target = "avatar", source = "profile.avatar")
    JoinRequestResponse toResponse(JoinRequest joinRequest, UserProfileResponse profile);
}
