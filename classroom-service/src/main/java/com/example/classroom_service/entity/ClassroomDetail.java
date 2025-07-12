package com.example.classroom_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "classroom_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassroomDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @Column(name = "student_username")
    private String studentUsername;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

}
