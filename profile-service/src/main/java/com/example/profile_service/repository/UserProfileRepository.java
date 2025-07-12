package com.example.profile_service.repository;

import com.example.profile_service.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends Neo4jRepository<UserProfile, String> {
    Optional<UserProfile> findByUsername(String username);

    @Query("MATCH (u:UserProfile) WHERE u.username IN $usernames RETURN u")
    List<UserProfile> findAllByUsernames(List<String> usernames);

    @Query(
            value = "MATCH (up:UserProfile) " +
                    "WHERE toLower(up.username) CONTAINS toLower($q) " +
                    "   OR toLower(up.fullName) CONTAINS toLower($q) " +
                    "   OR toLower(up.email) CONTAINS toLower($q) " +
                    "RETURN up",
            countQuery = "MATCH (up:UserProfile) " +
                    "WHERE toLower(up.username) CONTAINS toLower($q) " +
                    "   OR toLower(up.fullName) CONTAINS toLower($q) " +
                    "   OR toLower(up.email) CONTAINS toLower($q) " +
                    "RETURN count(up)"
    )
    Page<UserProfile> searchUsers(@Param("q") String q, Pageable pageable);
}
