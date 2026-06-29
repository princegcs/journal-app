package com.princegcs.JournalApplication.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @Test
    public void testSendEmail(){
        emailService.sendEmail(
                "prirp21@gmail.com",
                "JavaMailSender Test",
                "Hi, this is a test Email");
    }



}
