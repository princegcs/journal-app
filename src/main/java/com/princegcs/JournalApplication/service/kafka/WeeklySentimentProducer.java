package com.princegcs.JournalApplication.service.kafka;

import com.princegcs.JournalApplication.constant.KafkaTopics;
import com.princegcs.JournalApplication.model.WeeklySentimentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeeklySentimentProducer {

    private final KafkaTemplate<String, WeeklySentimentEvent> kafkaTemplate;

    public void publish(WeeklySentimentEvent event) {

        kafkaTemplate.send(KafkaTopics.WEEKLY_SENTIMENT, event);

        log.info("Published weekly sentiment event for {}", event.getEmail());
    }
}