package com.rocksolid.service;

public interface UserConferenceService {
  void addUserToConference(Long userId, Long conferenceId);
  boolean isUserInConference(Long userId, Long conferenceId);
}
