package com.rocksolid.service;

import com.rocksolid.dto.UserResponseDto;

import java.util.List;

public interface UserConferenceService {
  void addUserToConference(Long userId, Long conferenceId);
  boolean isUserInConference(Long userId, Long conferenceId);
  List<UserResponseDto> getUsersForConference(Long conferenceId);
  void deleteUserFromConference(Long userId, Long conferenceId);
}
