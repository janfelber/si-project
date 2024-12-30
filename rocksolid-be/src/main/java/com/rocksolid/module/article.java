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
    private int id;

    private String article_name;

    private String co_authors;

    private String article_description;

    private String key_words;

    //TODO - add column to storing file
    // private String article_file;

    @Lob
    @Column(name = "file",columnDefinition = "BYTEA",length = 1000000)
    private byte[] file;

    @ManyToOne
    @JoinColumn(name = "\"user_id\"")
    private User user;
}
