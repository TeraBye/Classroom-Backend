package com.example.search_service.controller;

import com.example.search_service.dto.response.ApiResponse;
import com.example.search_service.dto.response.SearchResponse;
import com.example.search_service.service.SearchService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchController {
    SearchService searchService;

    @GetMapping
    public ApiResponse<SearchResponse> search(
            @RequestParam("q") String q,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return ApiResponse.<SearchResponse>builder()
                .result(searchService.search(q, page, size))
                .build();
    }
}
