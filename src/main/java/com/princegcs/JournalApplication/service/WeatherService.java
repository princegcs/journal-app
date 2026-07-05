package com.princegcs.JournalApplication.service;

import com.princegcs.JournalApplication.dto.WeatherResponseDTO;
import com.princegcs.JournalApplication.entity.WeatherInfo;
import com.princegcs.JournalApplication.exception.WeatherApiException;
import com.princegcs.JournalApplication.external.weather.WeatherApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String weatherUrl;

    private final RestTemplate restTemplate;
    private final RedisService redisService;
    private static final String WEATHER_CACHE_PREFIX = "weather:";

    private String getCacheKey(String city) {
        return WEATHER_CACHE_PREFIX + city.toLowerCase();
    }


    public WeatherResponseDTO getWeather(String city) {

        String key = getCacheKey(city);
        WeatherResponseDTO cachedWeather = redisService.get(key, WeatherResponseDTO.class);

        if (cachedWeather != null) {
            log.info("Weather cache hit for city: {}", city);
            return cachedWeather;
        }

        WeatherApiResponse response = fetchWeather(city);
        WeatherResponseDTO weather = mapToResponseDTO(response);

        if (weather != null) {
            redisService.set(key, weather, Duration.ofMinutes(10));
        }

        return weather;
    }

    public WeatherInfo getWeatherInfo(String city) {
        WeatherApiResponse response = fetchWeather(city);
        return mapToWeatherInfo(response);
    }

    private WeatherResponseDTO mapToResponseDTO(WeatherApiResponse response){

            WeatherResponseDTO dto = new WeatherResponseDTO();
            dto.setCity(response.getLocation().getCity());
            dto.setTemperature(response.getCurrent().getTempC());
            dto.setCondition(response.getCurrent().getCondition().getText());
    return dto;
    }

    private WeatherInfo mapToWeatherInfo(WeatherApiResponse response){
        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setCondition(response.getCurrent().getCondition().getText());
        weatherInfo.setTemperature(response.getCurrent().getTempC());
    return weatherInfo   ;
    }

    //API call
    private WeatherApiResponse fetchWeather(String city) {

        log.info("Fetching weather from external API for city: {}", city);

        String location = city + ", India";
        try {

            String finalUrl = UriComponentsBuilder
                    .fromUriString(weatherUrl)
                    .queryParam("key", apiKey)
                    .queryParam("q", location)
                    .toUriString();

            ResponseEntity<WeatherApiResponse> response =
                    restTemplate.exchange(
                            finalUrl,
                            HttpMethod.GET,
                            null,
                            WeatherApiResponse.class
                    );



            WeatherApiResponse apiResponse = response.getBody();

            if (apiResponse == null || apiResponse.getCurrent() == null) {
                return null;
            }


            return apiResponse;

        } catch (Exception e) {

            log.error("Weather API request failed for city {}", city, e);

            throw new WeatherApiException(
                    "Failed to fetch weather information");
        }
    }
}