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

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private String meetLink;

    @Column(name = "is_public")
    private Boolean isPublic = true;

    private String teacherUsername;

    private String classCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
