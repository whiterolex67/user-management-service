package com.apica.userManagementService.service.impl;




import com.apica.userManagementService.dto.UserRequestDTO;
import com.apica.userManagementService.dto.UserResponseDTO;
import com.apica.userManagementService.exception.UsernameException;
import com.apica.userManagementService.model.User;
import com.apica.userManagementService.repository.UserRepository;
import com.apica.userManagementService.service.UserService;

import com.apica.userManagementService.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.apica.userManagementService.exception.UnauthorizedAccessException;


@Service
public class UserServiceImpl implements UserService {


    private final String REGISTER = " Registered Successfully";
    private final String UPDATE = " Updated Successfully";
    private final String DELETE = " Deleted Successfully";
    private final String FETCH = " fetched user data Successfully with username:";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {

        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new RuntimeException("Username already taken");
        }


        String encodedPassword = passwordEncoder.encode(userRequestDTO.getPassword());


        User user = User.builder()
                .username(userRequestDTO.getUsername())
                .password(encodedPassword)
                .email(userRequestDTO.getEmail())
                .roles(userRequestDTO.getRoles())
                .build();


        User savedUser = userRepository.save(user);


        kafkaProducer.sendUserEvent(savedUser,REGISTER);


        return mapToUserResponseDTO(savedUser);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        kafkaProducer.sendUserEvent(user,FETCH);
        return mapToUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userRequestDTO.getUsername() != null) {
            throw new UsernameException("username can't be changed");
        }

        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        if (!currentUser.getId().equals(id)) {
            throw new UnauthorizedAccessException("You are not allowed to update this user.");
        }

        user.setEmail(userRequestDTO.getEmail());
        user.setRoles(userRequestDTO.getRoles());
        if (userRequestDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }

        User updatedUser = userRepository.save(user);

        kafkaProducer.sendUserEvent(updatedUser,UPDATE);

        return mapToUserResponseDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        if (!currentUser.getId().equals(id)) {
            throw new UnauthorizedAccessException("You are not allowed to delete this user.");
        }

        String currentUsername = getCurrentUsername();

        if (!user.getUsername().equals(currentUsername)) {
            throw new UnauthorizedAccessException("You are not authorized to delete this user");
        }
        userRepository.delete(user);

        kafkaProducer.sendUserEvent(user,DELETE);
    }

    private UserResponseDTO mapToUserResponseDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRoles());
    }
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
