package com.princegcs.JournalApplication.controller;

import com.princegcs.JournalApplication.dto.UserResponseDTO;
import com.princegcs.JournalApplication.dto.UserUpdateDTO;
import com.princegcs.JournalApplication.dto.WeatherResponseDTO;
import com.princegcs.JournalApplication.service.UserService;
import com.princegcs.JournalApplication.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "User APIs",
        description = "Endpoints for user profile, account management, and preferences."
)
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final WeatherService weatherService;

    @GetMapping
    @Operation(
            summary = "Get User Profile",
            description = "Retrieves account settings, location profiles, and preference configurations for the logged-in user."
    )
    public ResponseEntity<UserResponseDTO> getCurrentUser(Authentication authentication) {

        String userName = authentication.getName();

        return ResponseEntity.ok(userService.getUserByUserName(userName));
    }

    //  Update user
    @PatchMapping
    @Operation(
            summary = "Update User Profile",
            description = "Modifies profile settings, city records, or password configurations"
    )
    public ResponseEntity<UserResponseDTO> updateUser(
            @Valid @RequestBody UserUpdateDTO dto,
            Authentication authentication) {

        String userName = authentication.getName();
        UserResponseDTO updated = userService.updateUser(dto, userName);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping
    @Operation(
            summary = "Delete User Account",
            description = "Wipes out the active user account details alongside all their correlated journals."
    )
    public ResponseEntity<Void> deleteUser(Authentication authentication) {
        String userName = authentication.getName();
        userService.deleteByUserName(userName);
        return ResponseEntity.noContent().build();


    }

    @GetMapping("/greeting")
    @Operation(
            summary = "Get Personalized Greeting",
            description = "Generates a customized user welcome message checking live system telemetry feed."
    )
    public ResponseEntity<String> greet(
            @RequestParam String city,
            Authentication authentication) {

        String userName = authentication.getName();

        WeatherResponseDTO weather = weatherService.getWeather(city);

        String greeting = "Namaste " + userName;

        if (weather != null) {

            greeting += ", weather in "
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