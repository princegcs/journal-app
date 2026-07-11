package com.princegcs.JournalApplication.service.kafka;

import com.princegcs.JournalApplication.constant.KafkaTopics;
import com.princegcs.JournalApplication.model.WeeklySentimentEvent;
import com.princegcs.JournalApplication.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeeklySentimentProducer {

    private final KafkaTemplate<String, WeeklySentimentEvent> kafkaTemplate;
    private final EmailService emailService;


    public void publish(WeeklySentimentEvent event){
        log.info("Publishing Weekly Sentiment Event");

        try{

            kafkaTemplate.send(KafkaTopics.WEEKLY_SENTIMENT, event).whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Kafka async send failed, falling back to email service", ex);
                    emailService.sendEmail(event.getEmail(), "Sentiment for last 7 days",
                            event.getSentiment().toString());
                }else {
                    log.info("Published weekly sentiment event for {}", event.getEmail());
                }
            });

        }catch (Exception e){
            log.error("Kafka sync failed, falling back to email service", e);
            emailService.sendEmail(event.getEmail(), "Sentiment for last 7 days",
                    event.getSentiment().toString());
        }
    }
}


