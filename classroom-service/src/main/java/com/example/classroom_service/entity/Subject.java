package com.example.classroom_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subject")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", unique = true)
    private String name;

}
