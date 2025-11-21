package com.example.import_service.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.kafka.topic")
@Getter
@Setter
public class TopicConfig {
    private String question;
    private String subject;
    private String user;
}
