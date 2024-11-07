package com.rocksolid.module;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "token", schema = "rock_solid")
public class token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int token_id;

    private String token;

    private String tokentype;

    private boolean expired;

    private boolean revoked;
}
