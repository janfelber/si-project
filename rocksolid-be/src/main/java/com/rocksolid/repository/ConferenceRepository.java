package com.rocksolid.repository;

import java.util.List;

import com.rocksolid.module.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRepository extends JpaRepository<Conference,Long> {
  List<Conference> findByStatusTrue();
}
