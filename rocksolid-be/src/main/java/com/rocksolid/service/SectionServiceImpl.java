package com.rocksolid.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    return sectionRepository.findAll(Sort.by(Sort.Order.asc("id"))).stream()
        .map(sections -> new SectionResponseDTO(sections.getId(), sections.getName())).collect(Collectors.toList());
  }

  public Optional<Sections> getSectionById(Long id) {
    return Optional.of(sectionRepository.findById(id).orElseThrow());
  }

  @Override
  public ResponseEntity<Void> createCategory(final SectionResponseDTO request) {
    final Sections sections = new Sections();
    sections.setName(request.getSectionName());
    sectionRepository.save(sections);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Override
  public Sections updateCategory(final Long id, final Sections sections) {
    Optional<Sections> category = sectionRepository.findById(id);
    if (category.isPresent()) {
      Sections existingCategory = category.get();
      existingCategory.setName(sections.getName());
      System.out.println(existingCategory.getName());
      return sectionRepository.save(existingCategory);
    }
    return null;
  }

}
