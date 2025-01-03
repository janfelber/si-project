package com.rocksolid.controller;


import com.rocksolid.auth.CurrentUserService;
import com.rocksolid.dto.ConferenceRequestDto;
import com.rocksolid.dto.ConferenceResponseDto;
import com.rocksolid.module.User;
import com.rocksolid.module.conference;
import com.rocksolid.repository.UserConferenceRepository;
import com.rocksolid.service.ConferenceService;
import com.rocksolid.service.UserConferenceService;

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
    private final UserConferenceService userConferenceService;
    private final CurrentUserService currentUserService;
    private final UserConferenceRepository userConferenceRepository;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('admin:read')")
    public List<ConferenceResponseDto> getAllConferences() {
        return conferenceService.getAllConferences();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
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

    @PostMapping("/addUserToConference")
    public ResponseEntity<String> addUserToConference(@RequestParam Long conferenceId) {
        userConferenceService.addUserToConference(currentUserService.getCurrentUserId(), conferenceId);
        return ResponseEntity.ok("User added to conference successfully");
    }

    @GetMapping("/isUserInConference")
    public ResponseEntity<String> isUserInConference(@RequestParam Long conferenceId) {
        boolean isInConference = userConferenceService.isUserInConference(currentUserService.getCurrentUserId(), conferenceId);
        if (isInConference) {
            return ResponseEntity.ok("User is in the conference");
        } else {
            return ResponseEntity.ok("User is not in the conference");
        }
    }

}
