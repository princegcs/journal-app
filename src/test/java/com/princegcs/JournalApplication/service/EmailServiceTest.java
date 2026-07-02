package com.princegcs.JournalApplication.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Disabled
    @Test
    void shouldSendEmail() {
        emailService.sendEmail(
                "princegcsn@email.com",
                "JavaMailSender Test",
                "Hi, this is a test email");
    }
}