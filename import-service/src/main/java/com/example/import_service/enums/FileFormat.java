package com.example.import_service.enums;

import org.springframework.web.multipart.MultipartFile;

public enum FileFormat {
    EXCEL(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    CSV(".csv", "text/csv"),
    PDF(".pdf", "application/pdf"),
    WORD(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

    private final String extension;
    private final String mimeType;

    FileFormat(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    // Method to detect from file
    public static FileFormat detect(MultipartFile file) {
        String contentType = file.getContentType();
        String name = file.getOriginalFilename().toLowerCase();
        // Logic detect based on mime or extension
        if ("xlsx".contains(contentType) || name.endsWith(".xlsx")) return EXCEL;
        if ("csv".contains(contentType) || name.endsWith(".csv")) return CSV;
        if ("pdf".contains(contentType) || name.endsWith(".pdf")) return PDF;
        if ("docx".contains(contentType) || name.endsWith(".docx")) return WORD;
        // Tương tự cho others
        throw new IllegalArgumentException("Unsupported format");
    }
}
