package com.rocksolid.repository;

import com.rocksolid.module.User;
import com.rocksolid.security.enums.Role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    // Optional<User> findByUsername(String username);

    List<User> findByRole(Role role);
}
