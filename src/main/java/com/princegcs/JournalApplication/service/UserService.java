package com.princegcs.JournalApplication.service;

import com.princegcs.JournalApplication.dto.UserRequestDTO;
import com.princegcs.JournalApplication.dto.UserResponseDTO;
import com.princegcs.JournalApplication.dto.UserUpdateDTO;
import com.princegcs.JournalApplication.entity.User;
import com.princegcs.JournalApplication.exception.ResourceNotFoundException;
import com.princegcs.JournalApplication.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    private UserResponseDTO mapToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId().toHexString());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setCity(user.getCity());
        dto.setSentiment(user.isSentimentAnalysis());
        return dto;
    }

    //internal method to be used by journal service.
    public User saveUser(User user) {
        return userRepo.save(user);
    }

    private UserResponseDTO createUserWithRole(
            UserRequestDTO dto,
            List<String> roles){

        User user = new User();
        user.setUserName(dto.getUserName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(roles);
        user.setEmail(dto.getEmail());
        user.setCity(dto.getCity());
        user.setSentimentAnalysis(dto.isSentimentAnalysis());
        User savedUser = userRepo.save(user);

        log.info("User created: {}", savedUser.getUserName());

        return mapToDTO(savedUser);

    }

    public UserResponseDTO createUser(UserRequestDTO dto){
        return createUserWithRole(dto, List.of("USER"));
    }

    public UserResponseDTO saveAdminUser(UserRequestDTO dto){
      return createUserWithRole(dto, List.of("USER", "ADMIN"));
    }

    public List<UserResponseDTO> getAllUsers(){
        List<User> all = userRepo.findAll();
        return all.stream().map(this::mapToDTO).toList();
    }

    public UserResponseDTO getUserById(ObjectId id){
        User user = userRepo.findById(id).orElseThrow(() -> {
            log.warn("User not found: {}", id);
            return new ResourceNotFoundException("User not found");
        });

    return mapToDTO(user);

    }

    public UserResponseDTO updateUser(UserUpdateDTO dto, String userName){
        User user = userRepo.findByUserName(userName).orElseThrow(() -> {
                    log.warn("User not found: {}", userName);
                    return new ResourceNotFoundException("User not found");
                });

        if (dto.getUserName() != null && !dto.getUserName().isEmpty()) {
            user.setUserName(dto.getUserName());
        }

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if(dto.getCity() != null && !dto.getCity().isEmpty()){
            user.setCity(dto.getCity());
        }

        if(dto.getEmail() != null && !dto.getEmail().isEmpty()){
            user.setEmail(dto.getEmail());
        }

       if (dto.isSentimentAnalysis()){
           user.setSentimentAnalysis(dto.isSentimentAnalysis());
       }

        User savedUser = userRepo.save(user);

        return mapToDTO(savedUser);
    }


//internal method, used by journalService.
    public User findByUserName(String userName) {
        return userRepo.findByUserName(userName).orElseThrow(() -> {
                    log.warn("User not found: {}", userName);
                    return new ResourceNotFoundException("User not found");
                });
    }

    public void deleteByUserName(String userName){
       User user =  userRepo.findByUserName(userName).orElseThrow(() -> {
                    log.warn("User not found: {}", userName);
                    return new ResourceNotFoundException("User not found");
                });
       userRepo.delete(user);
    }

    public UserResponseDTO getUserByUserName(String userName){
        User user =  userRepo.findByUserName(userName).orElseThrow(() -> {
                    log.warn("User not found: {}", userName);
                    return new ResourceNotFoundException("User not found");
                });
    return mapToDTO(user);
    }
}
