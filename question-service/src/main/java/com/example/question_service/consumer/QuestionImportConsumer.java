package com.example.question_service.consumer;

import com.example.question_service.dto.QuestionImportDTO;
import com.example.question_service.entity.Question;
import com.example.question_service.entity.QuestionVersion;
import com.example.question_service.enums.Level;
import com.example.question_service.event.ImportEvent;
import com.example.question_service.event.ImportResultEvent;
import com.example.question_service.mapper.QuestionMapper;
import com.example.question_service.repository.QuestionRepository;
import com.example.question_service.repository.QuestionVersionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionImportConsumer {
    QuestionRepository questionRepository;
    QuestionVersionRepository versionRepository;
    KafkaTemplate<String, Object> kafkaTemplate;
    QuestionMapper questionMapper;
    ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(
            topics = "import.question",
            groupId = "question-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void consume(ImportEvent<?> event) { // ImportEvent<QuestionImportDTO> event
        log.info("Received record: {}", event);

        try {
            List<Question> savedQuestions = new ArrayList<>();
            List<QuestionVersion> savedVersions = new ArrayList<>();
            List<QuestionImportDTO> records = objectMapper.convertValue(event.getRecords(), new TypeReference<List<QuestionImportDTO>>() {});

            for (QuestionImportDTO dto : records) {
                Question question = questionMapper.toQuestion(dto);
                question.setCreatedAt(LocalDateTime.now());
                question.setUpdatedAt(LocalDateTime.now());
                savedQuestions.add(question);

                QuestionVersion version = questionMapper.toQuestionVersion(dto);
                version.setQuestion(question);
                version.setVersion(1);
                version.setUpdatedBy(dto.getUsername());
                version.setCreatedAt(LocalDateTime.now());
                version.setLevel(Level.valueOf(dto.getLevel()));
                savedVersions.add(version);
            }
            questionRepository.saveAll(savedQuestions);
            versionRepository.saveAll(savedVersions);
            kafkaTemplate.send("import.result", new ImportResultEvent(event.getJobId(), "SUCCESS", null));
        } catch (Exception e) {
            kafkaTemplate.send("import.result", new ImportResultEvent(event.getJobId(), "FAILED", e.getMessage()));
        }
    }
}
