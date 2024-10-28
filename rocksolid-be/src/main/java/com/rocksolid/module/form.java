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
@Table(name = "form", schema = "rock_solid")
public class form {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int form_id;

    private String formcol;
}
