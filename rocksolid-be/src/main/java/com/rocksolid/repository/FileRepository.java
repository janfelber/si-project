package com.rocksolid.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rocksolid.module.Article;


public interface FileRepository extends JpaRepository<Article,Integer> {
  // article findByarticle_name(String fileName);

}
