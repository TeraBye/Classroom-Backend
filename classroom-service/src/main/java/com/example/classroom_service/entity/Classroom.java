package com.example.classroom_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "classroom")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    private String meetLink;

    @Column(name = "is_public")
    private boolean isPublic = true;

    private String classCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_subject_id")
    private TeacherSubject teacherSubject;

}
