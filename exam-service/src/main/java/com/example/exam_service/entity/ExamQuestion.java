package com.example.exam_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exam_question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no")
    private Integer orderNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @Column(name = "question_id", nullable = false)
    private int questionId;
}
