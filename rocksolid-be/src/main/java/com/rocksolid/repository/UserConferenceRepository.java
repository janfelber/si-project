package com.rocksolid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rocksolid.module.User;
import com.rocksolid.module.UserConference;
import com.rocksolid.module.conference;

@Repository
public interface UserConferenceRepository extends JpaRepository<UserConference, Long> {

  boolean existsByUserAndConference(User user, conference conference);

}
