package com.princegcs.JournalApplication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Health API",
        description = "Health check endpoint for monitoring application availability."
)
@RestController
public class HealthCheck {

    @GetMapping("/ok")
    @Operation(summary = "System Health Check")
    public String healthCheck() {
        return "🚀 Journal API is up and running. Ready to write, analyze, and speak!";
    }
}
