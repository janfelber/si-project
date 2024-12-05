package com.rocksolid.controller;

import com.rocksolid.dto.UserResponseDto;
import com.rocksolid.module.User;
import com.rocksolid.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/user")
public class UserController {
    private final UserService userService;

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('admin:read')")
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public Optional<UserResponseDto> getUser(@PathVariable Long id) { return userService.getUserById(id); }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userData) {
       return userService.updateUser(id, userData);
    }
}
