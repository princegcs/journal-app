package com.princegcs.JournalApplication.model;

import com.princegcs.JournalApplication.enums.Sentiment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklySentimentEvent {
    private String email;
    private Sentiment sentiment;
}
