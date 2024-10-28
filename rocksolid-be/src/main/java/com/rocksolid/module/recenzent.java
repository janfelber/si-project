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
@Table(name = "recenzent", schema = "rock_solid")
public class recenzent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int article_id;

    private int user_id;
}
