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

        // Extract base filename and extension
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String baseFilename = originalFilename.substring(0, originalFilename.lastIndexOf("."));

        // Construct unique public_id
        String publicId = role + "_" + username + "_";
        if (assignmentId.isPresent()) {
            publicId += assignmentId.get() + "_";
        } else {
            publicId += Instant.now().toEpochMilli() + "_";
        }
        publicId += originalFilename;

        // Upload file with resource_type "raw" for non-image files
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "folder", folder + "/" + role,
                "public_id", publicId,
                "resource_type", "raw", // Explicitly set to raw for PDFs, ZIPs, DOCX
                "access_mode", "public"
        ));

        // Lấy URL

        return (String) uploadResult.get("secure_url");
    }

    @Override
    public void deleteFile(String fileUrl) throws IOException, GeneralSecurityException {
        if (fileUrl == null) {
            return;
        }

        Cloudinary cloudinary = getCloudinary();

        // Extract public_id from fileUrl
        String publicId = extractPublicIdFromUrl(fileUrl);

        // Delete the file from Cloudinary
        cloudinary.uploader().destroy(publicId, ObjectUtils.asMap(
                "resource_type", "raw"
        ));
    }

    private String extractPublicIdFromUrl(String fileUrl) {
        // Example URL: https://res.cloudinary.com/<cloud_name>/raw/upload/v1234567890/<folder>/<public_id>.pdf
        String[] parts = fileUrl.split("/");
        String fileName = parts[parts.length - 1];
        String publicId = folder + "/TEACHER/" + fileName.substring(0, fileName.lastIndexOf("."));
        return publicId;
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
