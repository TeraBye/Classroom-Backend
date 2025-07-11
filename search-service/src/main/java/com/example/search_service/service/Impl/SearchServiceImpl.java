package com.example.search_service.service.Impl;

import com.example.search_service.dto.response.ApiResponse;
import com.example.search_service.dto.response.ClassroomResponse;
import com.example.search_service.dto.response.SearchResponse;
import com.example.search_service.dto.response.UserProfileResponse;
import com.example.search_service.repository.httpclient.ClassroomClient;
import com.example.search_service.repository.httpclient.ProfileClient;
import com.example.search_service.service.SearchService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchServiceImpl implements SearchService {
    ProfileClient profileClient;
    ClassroomClient classroomClient;
    @Override
    public SearchResponse search(String q, int page, int size) {
        /*
        * CompletableFuture để gọi hai API song song, bat đồng bộ
        * supplyAsync chạy tác vụ này trong một thread pool, giúp tránh chặn (blocking) thread chính của ứng dụng
        * */
        CompletableFuture<ApiResponse<Page<UserProfileResponse>>> userFuture =
                CompletableFuture.supplyAsync(() -> profileClient.searchUsers(q, page, size));

        CompletableFuture<ApiResponse<Page<ClassroomResponse>>> classroomFuture =
                CompletableFuture.supplyAsync(() -> classroomClient.searchClassrooms(q, page, size));

        /*Dòng này đảm bảo rằng cả hai tác vụ bất đồng bộ (userFuture và classroomFuture)
        đều hoàn thành trước khi tiếp tục xử lý.*/
        CompletableFuture.allOf(userFuture, classroomFuture).join();

        return SearchResponse.builder()
                .users(userFuture.join().getResult())
                .classrooms(classroomFuture.join().getResult())
                .build();
    }
}
