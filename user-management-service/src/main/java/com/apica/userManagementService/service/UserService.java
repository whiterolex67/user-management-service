package com.apica.userManagementService.service;


import com.apica.userManagementService.dto.UserRequestDTO;
import com.apica.userManagementService.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    UserResponseDTO getUserById(Long id);

    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO);

    void deleteUser(Long id);
}
