package com.apica.userManagementService.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserRequestDTO {
    private String username;
    private String password;
    private String email;
    private Set<String> roles;
}
