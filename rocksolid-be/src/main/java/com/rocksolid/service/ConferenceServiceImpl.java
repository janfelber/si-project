package com.rocksolid.service;

import com.rocksolid.dto.ConferenceRequestDto;
import com.rocksolid.dto.ConferenceResponseDto;
import com.rocksolid.module.Conference;
import com.rocksolid.repository.ConferenceRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
                        conference.getDatefrom(),
                        conference.getDateuntil(),
                        conference.getYear(),
                        conference.getName(),
                        conference.getStatus(),
                        conference.getDescription()
                )).collect(Collectors.toList());
    }

    public ResponseEntity<Void> createConference (ConferenceRequestDto request){
        Conference conference = new Conference();
        conference.setDatefrom(request.getDate_from());
        conference.setDateuntil(request.getDate_to());
        conference.setYear(request.getYear());
        conference.setName(request.getName());
        conference.setStatus(true);
        conferenceRepository.save(conference);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public List<Conference> getActiveConferences() {
        return conferenceRepository.findByStatusTrue();  // Získanie aktívnych konferencií
    }

    @Override
    public Optional<ConferenceResponseDto> getConferenceById(final Long id) {
        return conferenceRepository.findById(id)
            .map(conference -> new ConferenceResponseDto(
            conference.getId(),
            conference.getDatefrom(),
            conference.getDateuntil(),
            conference.getYear(),
            conference.getName(),
            conference.getStatus(),
            conference.getDescription()
        ));
    }

    public Conference updateConference(Long id, Conference conference){
        Optional<Conference> conf = conferenceRepository.findById(id);
        if(conf.isPresent()){
            Conference existingConference = conf.get();
            existingConference.setYear(conference.getYear());
            existingConference.setName(conference.getName());
            existingConference.setStatus(conference.getStatus());
            existingConference.setDateuntil(conference.getDateuntil());
            existingConference.setDatefrom(conference.getDatefrom());
            return conferenceRepository.save(existingConference);
        }
        return null;
    }
}
