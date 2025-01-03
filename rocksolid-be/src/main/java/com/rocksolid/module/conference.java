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
@Table(name = "conference", schema = "rock_solid")
public class conference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int year;

    //todo - add foreign key
    private Long article_id;

    //todo - add foreign key
    private Long form_id;

    private String datefrom;

    private String dateuntil;

    private String name;

    private boolean status;

    public boolean getStatus() {
        return status;
    }
}
