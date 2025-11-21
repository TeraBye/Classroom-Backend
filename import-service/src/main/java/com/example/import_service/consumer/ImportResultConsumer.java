package com.example.import_service.consumer;

import com.example.import_service.entity.ImportJob;
import com.example.import_service.enums.JobStatus;
import com.example.import_service.event.ImportResultEvent;
import com.example.import_service.repository.ImportJobRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImportResultConsumer {
    ImportJobRepository jobRepository;

    @KafkaListener(topics = "import.result", groupId = "import-service")
    public void consume(ImportResultEvent result) {
        log.info("Consumer received import result");
        ImportJob job = jobRepository.findById(result.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found: " + result.getJobId()));
        job.setStatus(JobStatus.valueOf(result.getStatus()));
        job.setMessage(result.getErrorMessage());
        jobRepository.save(job);
    }
}
