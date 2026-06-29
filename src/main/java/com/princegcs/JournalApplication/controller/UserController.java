package com.princegcs.JournalApplication.controller;

import com.princegcs.JournalApplication.dto.UserResponseDTO;
import com.princegcs.JournalApplication.dto.UserUpdateDTO;
import com.princegcs.JournalApplication.dto.WeatherResponseDTO;
import com.princegcs.JournalApplication.service.UserService;
import com.princegcs.JournalApplication.service.WeatherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<UserResponseDTO> getCurrentUser(Authentication authentication) {

        String userName = authentication.getName();

        return ResponseEntity.ok(userService.getUserByUserName(userName));
    }

    //  Update user
    @PatchMapping
    public ResponseEntity<UserResponseDTO> updateUser(
            @Valid @RequestBody UserUpdateDTO dto,
            Authentication authentication) {

        String userName = authentication.getName();
        UserResponseDTO updated = userService.updateUser(dto, userName);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(Authentication authentication){
        String userName = authentication.getName();
        userService.deleteByUserName(userName);
        return ResponseEntity.noContent().build();


    }

    @GetMapping("/greeting")
    public ResponseEntity<String> greet(
            @RequestParam String city,
            Authentication authentication) {

        String userName = authentication.getName();

        WeatherResponseDTO weather =
                weatherService.getWeather(city);

        String greeting = "Namaste " + userName;

        if (weather != null) {

            greeting +=
                    ", weather in "
                            + city
                            + " is "
                            + weather.getCondition()
                            + " ("
                            + weather.getTemperature()
                            + "°C)";
        }

        return ResponseEntity.ok(greeting);
    }
}