package com.example.import_service.enums;

import com.example.import_service.configuration.TopicConfig;

import java.util.Arrays;

public enum ImportType {
    QUESTION("question"),
    USER("user"),
    SUBJECT("subject");

    private final String topicKey;

    ImportType(String topicKey) {
        this.topicKey = topicKey;
    }

    public String getCode() {
        return topicKey;
    }

    public String getTopic(TopicConfig config) {
        return switch (this) {
            case QUESTION -> config.getQuestion();
            case SUBJECT -> config.getSubject();
            case  USER -> config.getUser();
        };
    }

    public static ImportType fromString(String text) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown import type: " + text));
    }
}
