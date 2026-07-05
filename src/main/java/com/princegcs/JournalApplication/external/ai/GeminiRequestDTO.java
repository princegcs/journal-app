package com.princegcs.JournalApplication.external.ai;

import lombok.Data;

@Data
public class GeminiRequestDTO {

    private String model;

    private String input;
}
