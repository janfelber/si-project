package com.rocksolid.service;

import com.rocksolid.dto.ConferenceResponseDto;
import com.rocksolid.dto.UserResponseDto;
import com.rocksolid.repository.ConferenceRepository;
import com.rocksolid.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConferenceServiceImpl  implements ConferenceService {
    private final ConferenceRepository conferenceRepository;

    public ConferenceServiceImpl(ConferenceRepository conferenceRepository) {
        this.conferenceRepository = conferenceRepository;
    }

    @Override
    public List<ConferenceResponseDto> getAllConferences() {
        return conferenceRepository.findAll()
                .stream()
                .map(conference -> new ConferenceResponseDto(
                        conference.getId(),
                        conference.getArticle_id(),
                        conference.getDatefrom(),
                        conference.getDateuntil(),
                        conference.getForm_id(),
                        conference.getYear(),
                        conference.getName(),
                        conference.getStatus()
                )).collect(Collectors.toList());
    }
}
