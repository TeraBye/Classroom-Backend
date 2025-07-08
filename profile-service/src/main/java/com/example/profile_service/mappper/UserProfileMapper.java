package com.example.profile_service.mappper;

import com.example.profile_service.dto.request.UserProfileCreationRequest;
import com.example.profile_service.dto.response.UserProfileResponse;
import com.example.profile_service.entity.UserProfile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(UserProfileCreationRequest userProfileCreationRequest);
    UserProfileResponse toUserProfileResponse(UserProfile userProfile);

    List<UserProfileResponse> toUserProfilesResponse(List<UserProfile> users);

}
