package com.example.assignment_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @Column(name = "submit_time")
    @JsonFormat(pattern = "HH:mm:ss dd:MM:yyyy")
    private LocalDateTime submitTime;

    @Column(name = "note")
    private String note;

    private String fileUrl;

    @Column(name = "student_username")
    private String studentUsername;

    @Column(name = "submission_count")
    @Builder.Default
    private Integer submissionCount = 0;

    @Version
    private Long version;

}
