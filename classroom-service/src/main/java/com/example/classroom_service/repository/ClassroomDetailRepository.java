package com.example.classroom_service.repository;

import com.example.classroom_service.entity.ClassroomDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomDetailRepository extends JpaRepository<ClassroomDetail, Integer> {
}