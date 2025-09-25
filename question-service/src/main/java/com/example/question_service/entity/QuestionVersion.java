package com.example.question_service.entity;

import com.example.question_service.enums.Level;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "question_version")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    private Integer version;  // Bắt đầu từ 1, tăng dần

    private String content;

    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    private String correctAnswer;

    private String explanation;

    @Enumerated(EnumType.STRING)
    private Level level;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    private String updatedBy;  // Người sửa (từ JWT hoặc auth context)
}
