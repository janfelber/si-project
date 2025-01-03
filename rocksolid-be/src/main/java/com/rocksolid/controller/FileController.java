package com.rocksolid.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rocksolid.module.article;
import com.rocksolid.service.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
@CrossOrigin(origins = "http://localhost:3000")
public class FileController {

  private final FileService fileService;

  @PostMapping("/upload")
  public ResponseEntity<article> uploadFile(
      @RequestParam("firstName") String firstName,
      @RequestParam("lastName") String lastName,
      @RequestParam("fileName") String fileName,
      @RequestParam("coAuthors") String coAuthors,
      @RequestParam("articleDescription") String articleDescription,
      @RequestParam("keyWords") String keyWords,
      @RequestParam("section") String section,
      @RequestParam("file") MultipartFile file,
      @RequestParam("conferenceId") Long conferenceId) {
    try {
      article savedFile = fileService.saveFile(firstName, lastName , fileName, coAuthors, articleDescription, keyWords,section,file, conferenceId);
      return ResponseEntity.ok(savedFile);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
    try {
      byte[] fileContent = fileService.loadFile(id);
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"file\"")
          .body(fileContent);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }



}
