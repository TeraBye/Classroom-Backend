package com.example.classroom_service.service;

import com.example.classroom_service.dto.request.SearchJoinRequest;
import com.example.classroom_service.dto.request.UpdateJoinRequest;
import com.example.classroom_service.dto.response.JoinRequestResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JoinRequestService {
    Page<JoinRequestResponse> getRequests(SearchJoinRequest request, Pageable pageable);
    JoinRequestResponse updateStatus(Integer requestId, UpdateJoinRequest request);

}
