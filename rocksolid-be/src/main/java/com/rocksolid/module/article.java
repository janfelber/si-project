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
@Table(name = "article", schema = "rock_solid")
public class article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String first_name;

    private String last_name;

    private String article_name;

    private String co_authors;

    private String article_description;

    private String key_words;

    private String section;

    //TODO - add column to storing file
    // private String article_file;

    private String file_path;

    @ManyToOne
    @JoinColumn(name = "\"user_id\"")
    private User user;
}
