package com.rocksolid.service;

import com.rocksolid.dto.ConferenceResponseDto;
import com.rocksolid.dto.UserResponseDto;

import java.util.List;

public interface ConferenceService {
    List<ConferenceResponseDto> getAllConferences();

}
