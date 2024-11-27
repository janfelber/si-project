package com.rocksolid.service;

import com.rocksolid.dto.UserResponseDto;
import com.rocksolid.module.User;

import java.util.List;

public interface UserService {
    void deleteUserById(Long id);
    List<UserResponseDto> getAllUsers();
}
