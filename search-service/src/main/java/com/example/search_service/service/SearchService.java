package com.example.search_service.service;

import com.example.search_service.dto.response.ClassroomResponse;
import com.example.search_service.dto.response.SearchResponse;
import com.example.search_service.dto.response.UserProfileResponse;
import org.springframework.data.domain.Page;

public interface SearchService {
    SearchResponse search(String q, int page, int size);
    Page<ClassroomResponse> searchClassrooms(String q, int page, int size);
    Page<UserProfileResponse> searchUsers(String q, int page, int size);
}
