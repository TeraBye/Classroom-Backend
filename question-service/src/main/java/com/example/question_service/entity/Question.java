package com.example.question_service.entity;

import com.example.question_service.enums.Level;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    private String content;
//
//    private String optionA;
//
//    private String optionB;
//
//    private String optionC;
//
//    private String optionD;
//
//    private String correctAnswer;
//
//    private String explanation;
//
//    @Enumerated(EnumType.STRING)
//    private Level level;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private String username;

    private Integer subjectId;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionVersion> versions = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "current_version_id")
    private QuestionVersion currentVersion;  // Trỏ đến version hiện tại

    private boolean locked = false;

}
