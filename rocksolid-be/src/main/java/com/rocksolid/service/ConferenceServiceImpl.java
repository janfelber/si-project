package com.rocksolid.service;

import com.rocksolid.dto.ConferenceRequestDto;
import com.rocksolid.dto.ConferenceResponseDto;
import com.rocksolid.dto.UserResponseDto;
import com.rocksolid.module.conference;
import com.rocksolid.repository.ConferenceRepository;
import com.rocksolid.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<Void> createConference (ConferenceRequestDto request){
        conference conference = new conference();
        conference.setArticle_id(request.getArticle_id());
        conference.setDatefrom(request.getDate_from());
        conference.setDateuntil(request.getDate_to());
        conference.setForm_id(request.getForm_id());
        conference.setYear(request.getYear());
        conference.setName(request.getName());
        conference.setStatus(true);
        conferenceRepository.save(conference);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
