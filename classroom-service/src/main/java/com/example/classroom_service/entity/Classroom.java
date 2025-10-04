package com.example.classroom_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    @Builder.Default
    private boolean isPublic = true;

    private String classCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_deleted")
    @Builder.Default
    private boolean isDeleted = false;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_subject_id")
    private TeacherSubject teacherSubject;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<ClassroomDetail> classroomDetails = new HashSet<>();

    public int getStudentCount() {
        return this.classroomDetails != null ? this.classroomDetails.size() : 0;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }
}
