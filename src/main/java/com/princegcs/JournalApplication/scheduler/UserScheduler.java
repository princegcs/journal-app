package com.princegcs.JournalApplication.scheduler;

import com.princegcs.JournalApplication.entity.JournalEntry;
import com.princegcs.JournalApplication.entity.User;
import com.princegcs.JournalApplication.enums.Sentiment;
import com.princegcs.JournalApplication.model.WeeklySentimentEvent;
import com.princegcs.JournalApplication.repository.UserRepo;
import com.princegcs.JournalApplication.service.EmailService;
import com.princegcs.JournalApplication.service.kafka.WeeklySentimentProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserScheduler {

    private final EmailService emailService;
    private final UserRepo userRepo;
    private final WeeklySentimentProducer weeklySentimentProducer;


    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUserAndSendEmail() {
        List<User> usersForSentimentAnalysis = userRepo.getUsersForSentimentAnalysis();
        for (User user : usersForSentimentAnalysis) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentimentList = journalEntries.stream()
                    .filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                    .map(JournalEntry::getSentiment).toList();

            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();

            for (Sentiment sentiment : sentimentList) {
                if (sentiment != null) {
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
                }
            }

            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;

            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }

            if (mostFrequentSentiment != null) {
                WeeklySentimentEvent event = WeeklySentimentEvent.builder()
                        .email(user.getEmail())
                        .sentiment(mostFrequentSentiment)
                        .build();
                weeklySentimentProducer.publish(event);
            }
        }
    }

}
