package com.example.identity_service.mapper;

import com.example.identity_service.dto.request.ProfileCreationRequest;
import com.example.identity_service.dto.request.UserRequestDTO;
import com.example.identity_service.dto.response.ProfileClientResponse;
import com.example.identity_service.dto.response.UserProfileResponse;
import com.example.identity_service.entity.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreationRequest toProfileCreationRequest(UserRequestDTO request);
    UserProfileResponse toUserProfileResponse(UserRequestDTO user);
    UserProfileResponse toUserProfileResponse(ProfileClientResponse user);
}
