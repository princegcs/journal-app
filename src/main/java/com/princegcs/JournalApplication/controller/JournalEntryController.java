package com.princegcs.JournalApplication.controller;

import com.princegcs.JournalApplication.dto.JournalRequestDTO;
import com.princegcs.JournalApplication.dto.JournalResponseDTO;
import com.princegcs.JournalApplication.dto.JournalUpdateDTO;
import com.princegcs.JournalApplication.service.JournalEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Journal APIs",
        description = "Endpoints for creating, retrieving, updating, and managing journal entries."
)
@RestController
@RequestMapping("/journal")
@RequiredArgsConstructor
public class JournalEntryController {

    private final JournalEntryService journalEntryService;

    // Get all entries of user
    @GetMapping
    @Operation(
            summary = "Retrieve All Journal Entries",
            description = "Fetches a complete list of journal entries belonging to the authenticated user"
    )
    public ResponseEntity<List<JournalResponseDTO>> getAll(Authentication authentication) {
        String userName = authentication.getName();
        List<JournalResponseDTO> entries = journalEntryService.getEntriesByUser(userName);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get Journal Entry by ID",
            description = "Fetches a single unique journal entry by its hexadecimal string ID."
    )
    public ResponseEntity<JournalResponseDTO> getById(
            @PathVariable String id,
            Authentication authentication) {

        String userName = authentication.getName();

        return ResponseEntity.ok(
                journalEntryService.getEntryByIdForUser(id, userName)
        );
    }

    // Create entry
    @PostMapping
    @Operation(
            summary = "Create Journal Entry",
            description = "Saves a new journal entry, automatically processes real-time weather logs, and analyzes context sentiment."
    )
    public ResponseEntity<JournalResponseDTO> createEntry(
            @Valid @RequestBody JournalRequestDTO dto,
            Authentication authentication) {
        String userName = authentication.getName();
        JournalResponseDTO response = journalEntryService.createEntry(dto, userName);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    //  Delete
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete Journal Entry",
            description = "Permanently removes a specific journal entry by its ID."
    )
    public ResponseEntity<Void> delete(
            @PathVariable String id,
            Authentication authentication) {

        String userName = authentication.getName();

        journalEntryService.deleteById(id, userName);
        return ResponseEntity.noContent().build();
    }

    // Update
    @PatchMapping("/{id}")
    @Operation(
            summary = "Update Journal Entry",
            description = "Partially updates an existing journal entry text content or title"
    )
    public ResponseEntity<JournalResponseDTO> update(
            @PathVariable String id,
            @RequestBody JournalUpdateDTO dto,
            Authentication authentication) {

        String userName = authentication.getName();

        JournalResponseDTO updated = journalEntryService.updateEntry(id, dto, userName);
        return ResponseEntity.ok(updated);
    }

    //Speech
    @PostMapping("/{id}/speech")
    @Operation(
            summary = "Generate Journal Audio",
            description = "Converts a specific journal entry into an audio file, so you can listen to it."
    )
    public ResponseEntity<byte[]> generateSpeech(
            @PathVariable String id,
            Authentication authentication) {

        byte[] audio = journalEntryService.generateSpeech(id, authentication.getName());

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("audio/mpeg"))
                .body(audio);
    }
}