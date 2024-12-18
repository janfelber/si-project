package com.rocksolid.service;

import com.rocksolid.dto.ConferenceRequestDto;
import com.rocksolid.dto.ConferenceResponseDto;
import com.rocksolid.dto.UserResponseDto;
import com.rocksolid.module.conference;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ConferenceService {
    List<ConferenceResponseDto> getAllConferences();
    List<conference> getActiveConferences();
    ResponseEntity<Void> createConference (ConferenceRequestDto request);
    conference updateConference(Long id, conference conference);
}
