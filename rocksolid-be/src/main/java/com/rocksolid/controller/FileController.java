package com.rocksolid.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rocksolid.dto.SectionResponseDTO;
import com.rocksolid.module.Sections;
import com.rocksolid.service.ArticleService;
import com.rocksolid.module.Article;
import com.rocksolid.service.FileService;
import com.rocksolid.service.SectionService;

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
      @RequestParam("file") MultipartFile file,
      @RequestParam("conferenceId") Long conferenceId) {
    try {
      Article savedFile = fileService.saveFile(firstName, lastName , fileName, coAuthors, articleDescription, keyWords,sectionId,file, conferenceId);
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

  @GetMapping("/download/{id}")
  public ResponseEntity<byte[]> getFileByArticleId (@PathVariable Long id) {
    try{
      byte[] file = fileService.getFileByArticleId(id);
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
      headers.setContentDisposition(ContentDisposition.attachment()
              .build());
      return new ResponseEntity<>(file, headers, HttpStatus.OK);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
  }

  @GetMapping("/fileName/{id}")
  public String getFileName (@PathVariable Long id) throws IOException {
    return fileService.getFileName(id);
  }
}
