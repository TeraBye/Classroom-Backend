package com.example.identity_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.identity_service.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    String username(String username);

    @Query("SELECT u FROM User u WHERE u.userId > :cursor ORDER BY u.userId ASC")
    List<User> findNextPage(@Param("cursor") int cursor, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.userId = :userId")
    Optional<User> getUserProfileByUserId(@Param("userId") int userId);
}
