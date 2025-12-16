package com.example.assignment_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "assignment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fileUrl;

    @JsonFormat(pattern = "HH:mm:ss dd:MM:yyyy")
    private LocalDateTime deadline;

    private String name;

    private String assignmentCode;

    private String username;

    @Column(name = "classroom_id")
    private Integer classroomId;

    @JsonFormat(pattern = "HH:mm:ss dd:MM:yyyy")
    private LocalDateTime startAt;

}
