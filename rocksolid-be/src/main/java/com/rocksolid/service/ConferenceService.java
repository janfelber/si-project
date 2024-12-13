package com.rocksolid.service;

import com.rocksolid.dto.ConferenceRequestDto;
import com.rocksolid.dto.ConferenceResponseDto;
import com.rocksolid.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ConferenceService {
    List<ConferenceResponseDto> getAllConferences();
    ResponseEntity<Void> createConference (ConferenceRequestDto request);
}
