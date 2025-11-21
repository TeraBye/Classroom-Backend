package com.example.import_service.service.impl;

import com.example.import_service.dto.response.ImportJobResponse;
import com.example.import_service.entity.ImportJob;
import com.example.import_service.enums.ImportType;
import com.example.import_service.enums.JobStatus;
import com.example.import_service.event.AuditLogEvent;
import com.example.import_service.event.ImportEvent;
import com.example.import_service.mapper.ImportJobMapper;
import com.example.import_service.parser.GenericExcelParser;
import com.example.import_service.parser.ParserRegistry;
import com.example.import_service.producer.ImportProducer;
import com.example.import_service.repository.ImportJobRepository;
import com.example.import_service.service.ImportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImportServiceImpl implements ImportService {
    ImportJobRepository jobRepository;
    ImportProducer producer;
    ImportJobMapper jobMapper;
    ParserRegistry parserRegistry;
    KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public Long handleImport(MultipartFile file, String username, String type) {
        ImportType importType = ImportType.valueOf(type.toUpperCase());
        // 1. Create & save PENDING job
        ImportJob job = ImportJob.builder()
                .type(importType)
                .status(JobStatus.PENDING)
                .username(username)
                .fileName(file.getOriginalFilename())
                .build();
        job = jobRepository.save(job);

        try {
            // 2. Parse file
            List<?> records = GenericExcelParser.parse(
                    file.getInputStream(),
                    parserRegistry.getParser(importType),
                    1
            );

            // 3. Update to PROCESSING
            job.setStatus(JobStatus.PROCESSING);
            jobRepository.save(job);

            // 4. Publish event
            ImportEvent<?> event = ImportEvent.builder()
                    .jobId(job.getId())
                    .username(username)
                    .type(importType.getCode())
                    .records((List<Object>) records)
                    .build();

            producer.publish(importType, event);

        } catch (Exception e) {
            // Publish FAILED
            job.setStatus(JobStatus.FAILED);
            job.setMessage(e.getMessage());
            jobRepository.save(job);

            producer.publish(importType, ImportEvent.builder()
                    .jobId(job.getId())
                    .build());

            throw new RuntimeException("Import failed: " + e.getMessage(), e);
        }
        AuditLogEvent logEvent = new AuditLogEvent(
                username,
                "TEACHER",
                "IMPORT FILE",
                "Import file " + type + " name: " + file.getOriginalFilename()
        );
        kafkaTemplate.send("audit.log", logEvent);
        return job.getId();
    }

    @Override
    public ImportJobResponse getJobStatus(Long jobId) {
        ImportJob job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        return jobMapper.toJobResponse(job);
    }
}
