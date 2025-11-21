package com.example.import_service.service;

import com.example.import_service.dto.response.ImportJobResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImportService {
    Long handleImport(MultipartFile file, String username, String type);
    ImportJobResponse getJobStatus(Long jobId);
}
