package com.example.profile_service.repository;

import com.example.profile_service.entity.UserProfile;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends Neo4jRepository<UserProfile,String> {
    Optional<UserProfile> findByUsername(String username);

    @Query("MATCH (u:UserProfile) WHERE u.username IN $usernames RETURN u")
    List<UserProfile> findAllByUsernames(List<String> usernames);
}
