package com.example.assignment_service.service.Impl;

import com.example.assignment_service.dto.request.AssignmentCreateRequest;
import com.example.assignment_service.dto.request.AssignmentUpdateRequest;
import com.example.assignment_service.dto.response.AssignmentResponse;
import com.example.assignment_service.entity.Assignment;
import com.example.assignment_service.mapper.AssignmentMapper;
import com.example.assignment_service.repository.AssignmentRepository;
import com.example.assignment_service.service.AssignmentService;
import com.example.assignment_service.service.FileStorageService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileStorageServiceImpl implements FileStorageService {
    @Value("${google.drive.credentials.file}")
    String credentialFile;
    @Value("${google.drive.folder.id}")
    String folderId;

    private Drive getDriveService() throws IOException, GeneralSecurityException {
        GoogleCredentials credential = GoogleCredentials.fromStream(
          FileStorageService.class.getResourceAsStream(credentialFile)
        ).createScoped(Collections.singleton("https://www.googleapis.com/auth/drive.file"));

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), (HttpRequestInitializer) credential)
                .setApplicationName("Assignment Submission App")
                .build();
    }
    @Override
    public String uploadFile(MultipartFile file, String studentUsername, Integer assignmentId) throws IOException, GeneralSecurityException {
        Drive driveService = getDriveService();

        String fileName = studentUsername + "_" + assignmentId + "_" +file.getOriginalFilename();

        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setParents(Collections.singletonList(folderId));

        InputStreamContent fileContent = new InputStreamContent(
                file.getContentType(),
                file.getInputStream()
        );

        File uploadedFile = driveService.files().create(fileMetadata, fileContent)
                .setFields("id, webViewLink")
                .execute();

        Permission permission = new Permission().setType("anyone").setRole("reader");
        driveService.permissions().create(uploadedFile.getId(), permission).execute();
        return uploadedFile.getWebViewLink();
    }
}
