package com.rocksolid.controller;


import com.rocksolid.dto.ConferenceResponseDto;
import com.rocksolid.dto.UserResponseDto;
import com.rocksolid.service.ConferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
}
