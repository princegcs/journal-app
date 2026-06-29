package com.princegcs.JournalApplication.controller;

import com.princegcs.JournalApplication.dto.JournalRequestDTO;
import com.princegcs.JournalApplication.dto.JournalResponseDTO;
import com.princegcs.JournalApplication.dto.JournalUpdateDTO;
import com.princegcs.JournalApplication.service.JournalEntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
@RequiredArgsConstructor
public class JournalEntryController {

    private final JournalEntryService journalEntryService;

    // Get all entries of user
    @GetMapping
    public ResponseEntity<List<JournalResponseDTO>> getAll(Authentication authentication) {
        String userName = authentication.getName();
        List<JournalResponseDTO> entries = journalEntryService.getEntriesByUser(userName);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalResponseDTO> getById(
            @PathVariable ObjectId id,
            Authentication authentication) {

        String userName = authentication.getName();

        return ResponseEntity.ok(
                journalEntryService.getEntryByIdForUser(id, userName)
        );
    }

    // Create entry
    @PostMapping
    public ResponseEntity<JournalResponseDTO> createEntry(
            @Valid @RequestBody JournalRequestDTO dto,
            Authentication authentication) {
        String userName = authentication.getName();
        JournalResponseDTO response = journalEntryService.createEntry(dto, userName);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    //  Delete
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable ObjectId id,
            Authentication authentication) {

        String userName = authentication.getName();

        journalEntryService.deleteById(id, userName);
        return ResponseEntity.noContent().build();
    }

    // Update
    @PatchMapping("/id/{id}")
    public ResponseEntity<JournalResponseDTO> update(
            @PathVariable ObjectId id,
            @RequestBody JournalUpdateDTO dto,
            Authentication authentication) {

        String userName = authentication.getName();

        JournalResponseDTO updated = journalEntryService.updateEntry(id, dto, userName);
        return ResponseEntity.ok(updated);
    }

    //Speech
    @PostMapping("/{id}/speech")
    public ResponseEntity<byte[]> generateSpeech(
            @PathVariable ObjectId id,
            Authentication authentication) {

        byte[] audio = journalEntryService.generateSpeech(id, authentication.getName());

        return ResponseEntity.ok()
//                .header(
//                        HttpHeaders.CONTENT_DISPOSITION,
//                        "attachment; filename=speech.mp3"
//                )
                .contentType(MediaType.valueOf("audio/mpeg"))
                .body(audio);
    }
}