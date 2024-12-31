package com.rocksolid.service;

import com.rocksolid.dto.ConferenceRequestDto;
import com.rocksolid.dto.ConferenceResponseDto;
import com.rocksolid.module.conference;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ConferenceService {
    List<ConferenceResponseDto> getAllConferences();
    List<conference> getActiveConferences();
    Optional<ConferenceResponseDto> getConferenceById(Long id);
    ResponseEntity<Void> createConference (ConferenceRequestDto request);
    conference updateConference(Long id, conference conference);
}
