package com.princegcs.JournalApplication.controller;

import com.princegcs.JournalApplication.dto.UserRequestDTO;
import com.princegcs.JournalApplication.dto.UserResponseDTO;
import com.princegcs.JournalApplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin-journal")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable ObjectId id){

        return ResponseEntity.ok(userService.getUserById(id));

    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<UserResponseDTO> createAdminUser(@RequestBody UserRequestDTO dto){
        UserResponseDTO response = userService.saveAdminUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
