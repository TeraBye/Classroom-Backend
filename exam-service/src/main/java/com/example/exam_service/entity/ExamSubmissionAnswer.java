package com.example.exam_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exam_submission_answer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamSubmissionAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private ExamSubmission submission;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "selected_option", length = 1)
    private String selectedOption;
}
