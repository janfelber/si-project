package com.rocksolid.controller;

import java.io.IOException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rocksolid.module.Article;
import com.rocksolid.service.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/file")
public class FileController {

  private final FileService fileService;

  @PostMapping("/upload")
  public ResponseEntity<Article> uploadFile(
      @RequestParam("firstName") String firstName,
      @RequestParam("lastName") String lastName,
      @RequestParam("fileName") String fileName,
      @RequestParam("coAuthors") String coAuthors,
      @RequestParam("articleDescription") String articleDescription,
      @RequestParam("keyWords") String keyWords,
      @RequestParam("sectionId") Long sectionId,
      @RequestParam("wordFile") MultipartFile wordFile,
      @RequestParam("pdfFile") MultipartFile pdfFile,
      @RequestParam("conferenceId") Long conferenceId) {
    try {
      Article savedFile = fileService.saveFile(firstName, lastName , fileName, coAuthors, articleDescription, keyWords, sectionId,
          wordFile, pdfFile, conferenceId);
      return ResponseEntity.ok(savedFile);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @PutMapping("/updateArticle")
  public ResponseEntity<Article> updateFile(
      @RequestParam("firstName") String firstName,
      @RequestParam("lastName") String lastName,
      @RequestParam("fileName") String fileName,
      @RequestParam("coAuthors") String coAuthors,
      @RequestParam("articleDescription") String articleDescription,
      @RequestParam("keyWords") String keyWords,
      @RequestParam("sectionId") Long sectionId,
      @RequestParam("wordFile") MultipartFile wordFile,
      @RequestParam("pdfFile") MultipartFile pdfFile,
      @RequestParam("conferenceId") Long conferenceId,
      @RequestParam("articleId") Long articleId) {
    try {
      Article savedFile = fileService.updateFile(firstName, lastName , fileName, coAuthors, articleDescription, keyWords, sectionId, wordFile, pdfFile ,conferenceId, articleId);
      return ResponseEntity.ok(savedFile);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping("/download/{id}/{fileType}")
  public ResponseEntity<byte[]> downloadPdfFile (
      @PathVariable final Long id,
      @PathVariable final String fileType) {
    try{
      final byte[] file = fileService.loadFile(id, fileType);
      final HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
      headers.setContentDisposition(ContentDisposition.attachment()
              .build());
      return new ResponseEntity<>(file, headers, HttpStatus.OK);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
  }

  @GetMapping("/fileName/{articleId}/{fileType}")
  public ResponseEntity<String> getFileName(
      @PathVariable final Long articleId,
      @PathVariable final String fileType) {
    try {
      final String fileName = fileService.getFileName(articleId, fileType);
      return new ResponseEntity<>(fileName, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>("Invalid file type: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (RuntimeException e) {
      return new ResponseEntity<>("Article not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
