package com.example.exam_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "exam")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="title")
    private String title;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "duration")
    private Integer duration; // thời lượng làm bài (phút)

    @Column(name = "number_of_question")
    private Integer numberOfQuestion;

    @Column(name = "begin_time")
    private LocalDateTime beginTime;


    @Column(name = "created_by")
    private String teacher;

    @Column(name = "class_id")
    private Integer classId;

    @Column(name = "subject_id")
    private Integer subjectId;
}