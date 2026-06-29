package com.princegcs.JournalApplication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {

    private String userName;


    @Email(message = "Invalid email format")
    private String email;

    private boolean sentimentAnalysis;


    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
            message = "Password must contain at least one uppercase, one lowercase, one digit, and one special character"
    )
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

}