package com.example.assignment_service.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface FileStorageService {
    String uploadFile(MultipartFile file, String studentUsername, Integer assignmentId) throws IOException, GeneralSecurityException;
}
