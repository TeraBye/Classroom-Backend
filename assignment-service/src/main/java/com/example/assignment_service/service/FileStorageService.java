package com.example.assignment_service.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

public interface FileStorageService {
    String uploadFile(MultipartFile file, String username, Optional<Integer> assignmentId, String role) throws IOException, GeneralSecurityException;
}
