package com.rocksolid.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rocksolid.dto.SectionResponseDTO;
import com.rocksolid.module.Sections;
import com.rocksolid.repository.SectionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {

  private final SectionRepository sectionRepository;

  @Override
  public List<SectionResponseDTO> getAllSectionNames() {

    return sectionRepository.findAll().stream()
        .map(sections -> new SectionResponseDTO(sections.getId(), sections.getName())).collect(Collectors.toList());
  }

  public Optional<Sections> getSectionById(Long id) {
    return Optional.of(sectionRepository.findById(id).orElseThrow());
  }

}
