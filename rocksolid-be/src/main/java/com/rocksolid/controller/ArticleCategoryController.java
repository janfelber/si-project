package com.rocksolid.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rocksolid.dto.SectionResponseDTO;
import com.rocksolid.module.Sections;
import com.rocksolid.service.SectionService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/category")
public class ArticleCategoryController {

  private SectionService sectionService;

  @GetMapping("/categories")
  public List<SectionResponseDTO> getAllSections() {
    return sectionService.getAllSectionNames();
  }


  @PostMapping("/create")
  public void createCategory(@RequestBody final SectionResponseDTO request) {
    System.out.println(request);
    sectionService.createCategory(request);
  }

  @PutMapping("/update/{id}")
  public Sections updateCategory(@PathVariable Long id, @RequestBody Sections request) {
    return sectionService.updateCategory(id, request);
  }

}
