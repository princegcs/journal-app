package com.princegcs.JournalApplication.service;

import com.princegcs.JournalApplication.external.ai.GeminiRequestDTO;
import com.princegcs.JournalApplication.external.ai.GeminiApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiService {

    @Value("${gemini.api.url}")
    private String url;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model}")
    private String model;

    private final RestTemplate restTemplate;

    public String askGemini(String prompt) {

        log.info("Calling Gemini for sentiment analysis");

        // request dto
        GeminiRequestDTO request = new GeminiRequestDTO();
        request.setModel(model);
        request.setInput(prompt);

        // headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        // request entity
        HttpEntity<GeminiRequestDTO> entity =
                new HttpEntity<>(request, headers);
        try {
            ResponseEntity<GeminiApiResponse> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            entity,
                            GeminiApiResponse.class

                    );

            log.debug("Gemini response received successfully");


            GeminiApiResponse body = response.getBody();

        if (body == null || body.getSteps() == null) {
            throw new RuntimeException("Invalid response from Gemini");
        }

        for (GeminiApiResponse.Step step : body.getSteps()) {

            if ("model_output".equals(step.getType())
                    && step.getContent() != null
                    && !step.getContent().isEmpty()) {

                return step.getContent()
                        .get(0)
                        .getText();
            }
        }
        }catch (Exception e){
            log.error("Error while calling Gemini API", e);

            throw e;
        }
        throw new RuntimeException("No model output received from Gemini");
    }

}

