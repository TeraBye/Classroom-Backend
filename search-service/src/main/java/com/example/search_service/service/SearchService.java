package com.example.search_service.service;

import com.example.search_service.dto.response.SearchResponse;

public interface SearchService {
    SearchResponse search(String q, int page, int size);
}
