package com.princegcs.JournalApplication.service;

import com.princegcs.JournalApplication.enums.Sentiment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SentimentAnalysisService {

    private final GeminiService geminiService;

    public Sentiment analyze(String journalText) {

        String prompt = buildPrompt(journalText);
        String response = geminiService.askGemini(prompt);
        return mapToSentiment(response);
    }

    private String buildPrompt(String journalText) {

        return """
                You are a sentiment classifier.

                Analyze the following journal entry.

                Return ONLY ONE WORD.
                
                Allowed values are ONLY:
                
                VERY_POSITIVE
                POSITIVE
                NEUTRAL
                NEGATIVE
                VERY_NEGATIVE
                
                Do not explain.
                Do not add punctuation.
                Do not add any extra words.
                
                Journal:
                %s
                """.formatted(journalText);
    }

    private Sentiment mapToSentiment(String response) {

        String result = response.trim().toUpperCase();
        result = result.replace(".", "");

        return switch (result) {
            case "VERY_POSITIVE" -> Sentiment.VERY_POSITIVE;
            case "POSITIVE" -> Sentiment.POSITIVE;
            case "NEUTRAL" -> Sentiment.NEUTRAL;
            case "NEGATIVE" -> Sentiment.NEGATIVE;
            case "VERY_NEGATIVE" -> Sentiment.VERY_NEGATIVE;
            default -> Sentiment.NEUTRAL;
        };
    }
}