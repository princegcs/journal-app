package com.princegcs.JournalApplication.service;


import com.princegcs.JournalApplication.exception.TextToSpeechException;
import com.princegcs.JournalApplication.external.TextToSpeech.TextToSpeechApiRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class TextToSpeechService {
    @Value("${elevenlabs.api.key}")
    private String apiKey;

    @Value("${elevenlabs.voice.id}")
    private String voiceId;

    @Value("${elevenlabs.api.url}")
    private String elevenLabsUrl;

    private final RestTemplate restTemplate;



    public byte[] generateSpeech(String text) {
        try {
            TextToSpeechApiRequest request = new TextToSpeechApiRequest();

            request.setText(text);
            request.setModelId("eleven_v3");


            HttpHeaders headers = new HttpHeaders();

            headers.set("xi-api-key", apiKey);

            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<TextToSpeechApiRequest> entity = new HttpEntity<>(request, headers);

            String url = elevenLabsUrl
                    + "/"
                    + voiceId
                    + "?output_format=mp3_44100_128";


            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    byte[].class
            );

            return response.getBody();

        } catch (Exception e) {

            log.error("Failed to generate speech using ElevenLabs API", e);

            throw new TextToSpeechException("Failed to generate speech from text");
        }
    }

}
