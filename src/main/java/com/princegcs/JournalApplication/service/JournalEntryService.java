package com.princegcs.JournalApplication.service;

import com.princegcs.JournalApplication.dto.JournalRequestDTO;
import com.princegcs.JournalApplication.dto.JournalResponseDTO;
import com.princegcs.JournalApplication.dto.JournalUpdateDTO;
import com.princegcs.JournalApplication.entity.JournalEntry;
import com.princegcs.JournalApplication.entity.User;
import com.princegcs.JournalApplication.enums.Sentiment;
import com.princegcs.JournalApplication.exception.ResourceNotFoundException;
import com.princegcs.JournalApplication.repository.JournalEntryRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@Slf4j
@Service
public class JournalEntryService {


    private final JournalEntryRepo journalEntryRepo;
    private final UserService userService;
    private final SentimentAnalysisService sentimentAnalysisService;
    private final TextToSpeechService textToSpeechService;


    // Entity → DTO
    private JournalResponseDTO mapToDTO(JournalEntry journalEntry) {
        JournalResponseDTO dto = new JournalResponseDTO();
        dto.setId(journalEntry.getId().toHexString());
        dto.setTitle(journalEntry.getTitle());
        dto.setContent(journalEntry.getContent());
        dto.setSentiment(journalEntry.getSentiment());
        dto.setDate(journalEntry.getDate());
        return dto;
    }

    private Sentiment analyzeSentiment(String content){
//        System.out.println("Inside analyzeSentiment");
        return sentimentAnalysisService.analyze(content);
    }

    //Saving Entry
    @Transactional
    public JournalResponseDTO createEntry(JournalRequestDTO dto, String userName){

        User user = userService.findByUserName(userName);

//        Sentiment sentiment = sentimentAnalysisService.analyze(dto.getContent());


        // DTO → Entity
        JournalEntry entry = new JournalEntry();
        entry.setTitle(dto.getTitle());
        entry.setContent(dto.getContent());
        entry.setDate(LocalDateTime.now());
        System.out.println("Before analyze");
        entry.setSentiment(analyzeSentiment(dto.getContent()));



        JournalEntry savedEntry = journalEntryRepo.save(entry);

        user.getJournalEntries().add(savedEntry);
        userService.saveUser(user);

        log.info("Journal entry saved for user: {}", userName);

        return mapToDTO(savedEntry);
    }




    //getByUser
    public List<JournalResponseDTO> getEntriesByUser(String userName) {

        User user = userService.findByUserName(userName);
        List<JournalEntry> allEntries = user.getJournalEntries();

        return allEntries.stream().map(entry -> mapToDTO(entry)).toList();
    }

    //get - entry - by - id
    public JournalResponseDTO getEntryByIdForUser(ObjectId id, String userName) {

        User user = userService.findByUserName(userName);

        boolean exists = user.getJournalEntries()
                .stream()
                .anyMatch(entry -> entry.getId().equals(id));

        if (!exists) {
            throw new ResourceNotFoundException("Entry not found for this user");
        }

        JournalEntry entry = journalEntryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entry not found"));

        return mapToDTO(entry);
    }


    //Update - Entry
    @Transactional
    public JournalResponseDTO updateEntry(ObjectId id, JournalUpdateDTO dto, String userName) {

        User user = userService.findByUserName(userName);

        Sentiment sentiment = sentimentAnalysisService.analyze(dto.getContent());


        boolean exists = user.getJournalEntries()
                .stream()
                .anyMatch(e -> e.getId().equals(id));

        if (!exists) {
            throw new AccessDeniedException("Journal entry does not belong to the current user");        }

        JournalEntry entry = journalEntryRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Entry not found: {}", id);
                    throw new ResourceNotFoundException("Entry not found for this user");                });

        if (dto.getTitle() != null && !dto.getTitle().isEmpty()) {
            entry.setTitle(dto.getTitle());
        }

        if (dto.getContent() != null && !dto.getContent().isEmpty()) {
            entry.setContent(dto.getContent());

            entry.setSentiment(analyzeSentiment(dto.getContent()));
        }


        JournalEntry updated = journalEntryRepo.save(entry);


        return mapToDTO(updated);
    }

    //Delete - BY - ID
    @Transactional
    public void deleteById(ObjectId id, String userName) {

        User user = userService.findByUserName(userName);

        boolean exists = user.getJournalEntries()
                .stream()
                .anyMatch(e -> id.equals(e.getId()));

        if (!exists) {
            throw new ResourceNotFoundException("Entry not found for this user");
        }

        user.getJournalEntries().removeIf(e -> id.equals(e.getId()));

        journalEntryRepo.deleteById(id);

        userService.saveUser(user);

        log.info("Deleted journal entry {} for user {}", id, userName);
    }


    //Text - TO - Speech

    public byte[] generateSpeech(ObjectId id, String userName) {

        User user = userService.findByUserName(userName);

        boolean exists = user.getJournalEntries()
                .stream()
                .anyMatch(entry -> entry.getId().equals(id));

        if (!exists) {
            throw new ResourceNotFoundException("Entry not found for this user");
        }

        JournalEntry entry = journalEntryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entry not found"));

        return textToSpeechService.generateSpeech(entry.getContent());
    }
}
