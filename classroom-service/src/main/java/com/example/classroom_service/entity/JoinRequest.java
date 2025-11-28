package com.example.classroom_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "join_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    private String studentUsername;

    private String teacherUsername;

    private String status;

    private LocalDateTime requestedAt;

    private LocalDateTime approvedAt;

}
