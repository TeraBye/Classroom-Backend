package com.example.import_service.entity;

import com.example.import_service.enums.ImportType;
import com.example.import_service.enums.JobStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Builder
@Getter
@Setter
@Table(name = "import_job")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ImportJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ImportType type;
    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.PENDING; // PENDING, PROCESSING, SUCCESS, FAILED
    @Column(length = 2048)
    private String message; // last error or info
    private String username;
    private String fileName;
    @CreatedDate
    private Instant createdAt = OffsetDateTime.now(ZoneOffset.UTC).toInstant();
    @LastModifiedDate
    private Instant updatedAt = OffsetDateTime.now(ZoneOffset.UTC).toInstant();
}
