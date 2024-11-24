package com.rocksolid.service;

import com.rocksolid.module.User;

import java.util.List;

public interface UserService {
    void deleteUserById(Long id);
    List<User> getAllUsers();
}
