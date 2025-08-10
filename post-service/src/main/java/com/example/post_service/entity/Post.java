package com.example.post_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "username", length = 45, nullable = false)
    private String username;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "assignment_id", length = 45)
    private Integer assignmentId;

    @Column(name = "class_id", length = 45)
    private Integer classId;
}
