package com.rocksolid.auth;

import com.rocksolid.security.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private String university;
}
