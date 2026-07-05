package com.princegcs.JournalApplication.dto;

import lombok.Data;

@Data
public class WeatherResponseDTO {
    private String city;
    private Double temperature;
    private String condition;
}