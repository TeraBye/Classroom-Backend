package com.example.assignment_service.service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.assignment_service.service.FileStorageService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileStorageServiceImpl implements FileStorageService {
    @Value("${cloudinary.cloud.name}")
    String cloudName;

    @Value("${cloudinary.api.key}")
    String apiKey;

    @Value("${cloudinary.api.secret}")
    String apiSecret;

    @Value("${cloudinary.folder}")
    String folder;

    private Cloudinary getCloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    @Override
    public String uploadFile(MultipartFile file, String username, Optional<Integer> assignmentId, String role) throws IOException, GeneralSecurityException {
        // Kiểm tra file
        validateFile(file);

        // Khởi tạo Cloudinary
        Cloudinary cloudinary = getCloudinary();

        // Tạo tên file duy nhất
        String fileName = role + "_" + username + "_";
        if (assignmentId.isPresent()) {
            fileName += assignmentId.get() + "_";
        } else {
            fileName += Instant.now().toEpochMilli() + "_"; // Dùng timestamp nếu không có assignmentId
        }

        fileName += file.getOriginalFilename();
        // Upload file
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "folder", folder + "/" + role,
                "public_id", fileName,
                "resource_type", "auto", // Hỗ trợ mọi loại file (PDF, ZIP, DOCX)
                "access_mode", "public" // Ai cũng có thể xem
        ));

        // Lấy URL
        String fileUrl = (String) uploadResult.get("secure_url");

        return fileUrl;
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        String[] allowedTypes = {"application/pdf", "application/zip", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};
        if (!Arrays.asList(allowedTypes).contains(file.getContentType())) {
            throw new IllegalArgumentException("Invalid file type. Only PDF, ZIP, and DOCX are allowed.");
        }

        if (file.getSize() > 10 * 1024 * 1024) { // 10MB
            throw new IllegalArgumentException("File size exceeds 10MB limit.");
        }
    }
}
