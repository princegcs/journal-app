package com.princegcs.JournalApplication.external.ai;

import lombok.Data;

import java.util.List;

@Data
public class GeminiApiResponse {

    private List<Step> steps;

    @Data
    public static class Step {

        private String type;

        private List<Content> content;
    }

    @Data
    public static class Content {

        private String text;
    }
}

