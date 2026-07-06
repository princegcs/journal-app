package com.princegcs.JournalApplication.controller;

import com.princegcs.JournalApplication.dto.JwtResponseDTO;
import com.princegcs.JournalApplication.dto.LoginRequestDTO;
import com.princegcs.JournalApplication.dto.UserRequestDTO;
import com.princegcs.JournalApplication.dto.UserResponseDTO;
import com.princegcs.JournalApplication.service.UserService;
import com.princegcs.JournalApplication.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
@Slf4j
public class PublicController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // Signup
    @PostMapping("/singnup")
    public ResponseEntity<UserResponseDTO> signup(
            @Valid @RequestBody UserRequestDTO dto) {

        UserResponseDTO response = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Login

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUserName(),
                        dto.getPassword()
                )
        );

        String token = jwtUtil.generateToken(dto.getUserName());

        return ResponseEntity.ok(new JwtResponseDTO(token));

    }

}
