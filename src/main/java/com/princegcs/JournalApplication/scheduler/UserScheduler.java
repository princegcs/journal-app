package com.princegcs.JournalApplication.scheduler;

import com.princegcs.JournalApplication.entity.JournalEntry;
import com.princegcs.JournalApplication.entity.User;
import com.princegcs.JournalApplication.enums.Sentiment;
import com.princegcs.JournalApplication.repository.UserRepo;
import com.princegcs.JournalApplication.service.EmailService;
import com.princegcs.JournalApplication.service.SentimentAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserScheduler {

    private final EmailService emailService;
    private final SentimentAnalysisService service;
    private final UserRepo userRepo;


    @Scheduled( cron = "0 0 9 *  * Sun")
    public void fetchUserAndSendEmail(){
        List<User> usersForSentimentAnalysis = userRepo.getUsersForSentimentAnalysis();
        for (User user : usersForSentimentAnalysis){
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentimentList = journalEntries.stream()
                    .filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                    .map(x -> x.getSentiment()).collect(Collectors.toList());

            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();

            for(Sentiment sentiment : sentimentList){
                if( sentiment != null ){
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
                }
            }

            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;

            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()){
                if(entry.getValue() > maxCount){
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }

            if( mostFrequentSentiment != null ) {

                emailService.sendEmail(user.getEmail(), "Sentiment for last 7 days", mostFrequentSentiment.toString());
            }
        }
    }

}
