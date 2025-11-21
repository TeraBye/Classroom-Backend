package com.example.import_service.producer;

import com.example.import_service.configuration.TopicConfig;
import com.example.import_service.enums.ImportType;
import com.example.import_service.event.ImportEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImportProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TopicConfig topicConfig;

    public void publish(ImportType type, ImportEvent<?> event) {
        log.info("Publishing import event for type {} and event {}", type, event);
        kafkaTemplate.send(type.getTopic(topicConfig), event);
    }
}
