package com.rocksolid.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.rocksolid.dto.ConferenceRequestDto;
import com.rocksolid.dto.SectionResponseDTO;
import com.rocksolid.module.Sections;

public interface SectionService {

  List<SectionResponseDTO> getAllSectionNames();
  Optional<Sections> getSectionById(Long id);
  ResponseEntity<Void> createCategory (SectionResponseDTO request);
  Sections updateCategory(Long id, Sections sections);

}
