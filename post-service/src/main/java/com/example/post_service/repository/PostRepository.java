package com.example.post_service.repository;

import com.example.post_service.dto.response.ClassPostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.post_service.entity.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByClassIdOrderByCreateAtDesc(Integer classId, Pageable pageable);

    @Query("SELECT new com.example.post_service.dto.response.ClassPostResponse(p.classId, COUNT(p.id)) " +
            "FROM Post p " +
            "WHERE p.classId IN :classIds " +
            "GROUP BY p.classId")
    List<ClassPostResponse> countPostsByClassIds(@Param("classIds") List<Integer> classIds);
}

