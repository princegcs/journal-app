package com.princegcs.JournalApplication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

    @GetMapping("/ok")
    public String healthCheck(){
        return "🚀 Journal API is up and running. Ready to write, analyze, and speak!";    }
}
