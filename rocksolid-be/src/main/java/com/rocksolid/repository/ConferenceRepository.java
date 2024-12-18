package com.rocksolid.repository;

import java.util.List;

import com.rocksolid.module.conference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRepository extends JpaRepository<conference,Long> {
  List<conference> findByStatusTrue();
}
