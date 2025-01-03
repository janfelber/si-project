package com.rocksolid.service;

import org.springframework.stereotype.Service;

import com.rocksolid.module.User;
import com.rocksolid.module.UserConference;
import com.rocksolid.module.conference;
import com.rocksolid.repository.ConferenceRepository;
import com.rocksolid.repository.UserConferenceRepository;
import com.rocksolid.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserConferenceImpl implements UserConferenceService {


  private final UserRepository userRepository;
  private final UserConferenceRepository userConferenceRepository;
  private final ConferenceRepository conferenceRepository;

  public void addUserToConference(Long userId, Long conferenceId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    conference conference = conferenceRepository.findById(conferenceId).orElseThrow(() -> new RuntimeException("Conference not found"));

    if (userConferenceRepository.existsByUserAndConference(user, conference)) {
      throw new RuntimeException("User already added to conference");
    } else {
      UserConference userConference = UserConference.builder()
          .user(user)
          .conference(conference)
          .build();
      userConferenceRepository.save(userConference);
    }

  }

  public boolean isUserInConference(Long userId, Long conferenceId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
    conference conference = conferenceRepository.findById(conferenceId)
        .orElseThrow(() -> new RuntimeException("Conference not found"));

    return userConferenceRepository.existsByUserAndConference(user, conference);
  }

}