package com.princegcs.JournalApplication.controller;

import com.princegcs.JournalApplication.dto.UserRequestDTO;
import com.princegcs.JournalApplication.dto.UserResponseDTO;
import com.princegcs.JournalApplication.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Admin APIs",
        description = "Administrative endpoints for user and system management."
)
@RestController
@RequestMapping("/admin-journal")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    @Operation(
            summary = "Get All Users List",
            description = "See a list of all registered users in the database."
    )
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/user/{id}")
    @Operation(
            summary = "Get Specific User by ID",
            description = "Look up and see the details of a specific user by using their ID."
    )
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable String id) {

        return ResponseEntity.ok(userService.getUserById(id));

    }

    @PostMapping("/create-admin-user")
    @Operation(
            summary = "Create an Admin User",
            description = "Create a new admin account in the system."
    )
    public ResponseEntity<UserResponseDTO> createAdminUser(@RequestBody UserRequestDTO dto) {
        UserResponseDTO response = userService.saveAdminUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
