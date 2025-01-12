package com.rocksolid.service;

import com.rocksolid.dto.ConferenceRequestDto;
import com.rocksolid.dto.ConferenceResponseDto;
import com.rocksolid.module.Conference;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ConferenceService {
    List<ConferenceResponseDto> getAllConferences();
    List<Conference> getActiveConferences();
    Optional<ConferenceResponseDto> getConferenceById(Long id);
    ResponseEntity<Void> createConference (ConferenceRequestDto request);
    Conference updateConference(Long id, Conference conference);
}
