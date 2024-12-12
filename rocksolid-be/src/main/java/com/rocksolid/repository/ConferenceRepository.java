package com.rocksolid.repository;

import com.rocksolid.module.conference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRepository extends JpaRepository<conference,Long> {

}
