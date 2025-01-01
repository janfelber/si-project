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

    public List<conference> getActiveConferences() {
        return conferenceRepository.findByStatusTrue();  // Získanie aktívnych konferencií
    }

    @Override
    public Optional<ConferenceResponseDto> getConferenceById(final Long id) {
        return conferenceRepository.findById(id)
            .map(conference -> new ConferenceResponseDto(
            conference.getId(),
            conference.getArticle_id(),
            conference.getDatefrom(),
            conference.getDateuntil(),
            conference.getForm_id(),
            conference.getYear(),
            conference.getName(),
            conference.getStatus()
        ));
    }

    public conference updateConference(Long id, conference conference){
        Optional<conference> conf = conferenceRepository.findById(id);
        if(conf.isPresent()){
            conference existingConference = conf.get();
            existingConference.setForm_id(conference.getForm_id());
            existingConference.setYear(conference.getYear());
            existingConference.setName(conference.getName());
            existingConference.setStatus(conference.getStatus());
            existingConference.setDateuntil(conference.getDateuntil());
            existingConference.setDatefrom(conference.getDatefrom());
            existingConference.setArticle_id(conference.getArticle_id());
            return conferenceRepository.save(existingConference);
        }
        return null;
    }
}
