package com.rocksolid.service;

import com.rocksolid.dto.UserResponseDto;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rocksolid.module.User;
import com.rocksolid.module.UserConference;
import com.rocksolid.module.Conference;
import com.rocksolid.repository.ConferenceRepository;
import com.rocksolid.repository.UserConferenceRepository;
import com.rocksolid.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserConferenceImpl implements UserConferenceService {


  private final UserRepository userRepository;
  private final UserConferenceRepository userConferenceRepository;
  private final ConferenceRepository conferenceRepository;

  public void addUserToConference(Long userId, Long conferenceId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    Conference conference = conferenceRepository.findById(conferenceId).orElseThrow(() -> new RuntimeException("Conference not found"));

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
    Conference conference = conferenceRepository.findById(conferenceId)
        .orElseThrow(() -> new RuntimeException("Conference not found"));

    return userConferenceRepository.existsByUserAndConference(user, conference);
  }

  public List<UserResponseDto> getUsersForConference(Long conferenceId) {
    return userConferenceRepository.findAll(Sort.by(Sort.Order.asc("id")))
            .stream()
            .filter(userConference -> userConference.getConference().getId().equals(conferenceId))
            .map(userConference -> {
              User user = userConference.getUser();
              return new UserResponseDto(
                      user.getId(),
                      user.getFirst_name(),
                      user.getEmail(),
                      user.getLast_name(),
                      user.getRole().name(),
                      user.getUniversity()
              );
            })
            .collect(Collectors.toList());
  }

  public void deleteUserFromConference(Long userId, Long conferenceId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    Conference conference = conferenceRepository.findById(conferenceId).orElseThrow(() -> new RuntimeException("Conference not found"));
    UserConference user_conference = userConferenceRepository.findByUserAndConference(user, conference);
    userConferenceRepository.delete(user_conference);
  }
}