package com.princegcs.JournalApplication.dto;

import com.princegcs.JournalApplication.entity.WeatherInfo;
import com.princegcs.JournalApplication.enums.Sentiment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class JournalResponseDTO {
    private String id;
    private String title;
    private String content;
    private Sentiment sentiment;
    private WeatherInfo weatherInfo;
    private LocalDateTime date;
}