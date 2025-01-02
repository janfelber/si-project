package com.rocksolid.controller;


import com.rocksolid.dto.ConferenceRequestDto;
import com.rocksolid.dto.ConferenceResponseDto;
import com.rocksolid.module.conference;
import com.rocksolid.service.ConferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/conference")
public class ConferenceController {

    private final ConferenceService conferenceService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('admin:read')")
    public List<ConferenceResponseDto> getAllConferences() {
        return conferenceService.getAllConferences();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('student:read') and hasAuthority('reviewer:read')")
    public Optional<ConferenceResponseDto> getConference(@PathVariable final Long id) {
        return conferenceService.getConferenceById(id);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<Void> createConference(@RequestBody ConferenceRequestDto request){
        return conferenceService.createConference(request);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public conference updateConference(@PathVariable Long id, @RequestBody conference request){
        return conferenceService.updateConference(id, request);
    }

    @GetMapping("/active")
    public List<conference> getActiveConferences() {
        return conferenceService.getActiveConferences();
    }
}
