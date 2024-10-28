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
    private int id;

    private int year;

    //todo - add foreign key
    private int article_id;

    //todo - add foreign key
    private int form_id;

    private String datefrom;

    private String dateuntil;

}
