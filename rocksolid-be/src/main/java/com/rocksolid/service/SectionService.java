package com.rocksolid.service;

import java.util.List;
import java.util.Optional;

import com.rocksolid.dto.SectionResponseDTO;
import com.rocksolid.module.Sections;

public interface SectionService {

  List<SectionResponseDTO> getAllSectionNames();
  Optional<Sections> getSectionById(Long id);

}
