package com.rocksolid.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String first_name;
    private String email;
    private String last_name;
    private String role;
    private String university;
}
