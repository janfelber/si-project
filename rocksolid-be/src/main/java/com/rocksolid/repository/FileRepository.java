package com.rocksolid.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rocksolid.module.article;


public interface FileRepository extends JpaRepository<article,Integer> {
  // article findByarticle_name(String fileName);

}
