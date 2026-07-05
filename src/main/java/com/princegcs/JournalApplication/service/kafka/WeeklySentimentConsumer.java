package com.princegcs.JournalApplication.service.kafka;

import com.princegcs.JournalApplication.model.WeeklySentimentEvent;
import com.princegcs.JournalApplication.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeeklySentimentConsumer {

    private final EmailService emailService;


    @KafkaListener(topics = "weekly-sentiment-events", groupId = "weekly-sentiment-group")
    public void consume(WeeklySentimentEvent event) {

        log.info("Received weekly sentiment event for {}", event.getEmail());

        emailService.sendEmail(event.getEmail(), "Sentiment for last 7 days", event.getSentiment().toString()
        );
    }
}