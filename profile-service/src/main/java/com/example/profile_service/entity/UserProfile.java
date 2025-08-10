package com.example.profile_service.entity;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.LocalDate;

@Node
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfile {
    private static final String DEFAULT_AVATAR =
            "https://i.pinimg.com/736x/6e/59/95/6e599501252c23bcf02658617b29c894.jpg";

    @Id
    @GeneratedValue(generatorClass =  UUIDStringGenerator.class)
    private String id;

    @Property("userId")
    private int userId;

    @Property("username")
    private String username;

    private String fullName;

    @Property("email")
    private String email;

    private LocalDate dob;

    private String avatar = DEFAULT_AVATAR;
}
