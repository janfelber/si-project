package com.rocksolid.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rocksolid.module.Sections;

public interface SectionRepository extends JpaRepository<Sections, Long> {

  // List<Sections> findAll();
  List<Sections> findAll();
  Optional<Sections> findById(Long id);


}
