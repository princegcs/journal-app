package com.princegcs.JournalApplication.external.TextToSpeech;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TextToSpeechApiRequest {

    private String text;

    @JsonProperty("model_id")
    private String modelId;
}