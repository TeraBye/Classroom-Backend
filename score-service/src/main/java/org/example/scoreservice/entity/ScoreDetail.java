package org.example.scoreservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.scoreservice.enums.TYPEOFSCORE;

@Entity
@Data
@Table(name = "score_detail")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScoreDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer scoreDetailId;

    Float score;

    Integer studentId;

    Integer classroomId;

    @Enumerated(EnumType.STRING)
    TYPEOFSCORE typeofscore;

}
