package com.rocksolid.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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

  @PreAuthorize("hasAuthority('admin:read')")
  @PostMapping("/zip")
  public ResponseEntity<ByteArrayResource> downloadZip(@RequestBody List<Long> Ids) {
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
        int namingCounter = 0;
        for (Long id : Ids) {
          namingCounter++;
          byte[] fileContent = fileService.loadFile(id, "word");
          String fileName = namingCounter + " - " + fileService.getFileName(id, "word");
          ZipEntry zipEntry = new ZipEntry(fileName);
          zipOutputStream.putNextEntry(zipEntry);
          zipOutputStream.write(fileContent);
          zipOutputStream.closeEntry();

          fileContent = fileService.loadFile(id, "pdf");
          fileName = namingCounter + "- " + fileService.getFileName(id, "pdf");
          zipEntry = new ZipEntry(fileName);
          zipOutputStream.putNextEntry(zipEntry);
          zipOutputStream.write(fileContent);
          zipOutputStream.closeEntry();

        }
      }

      ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=articles.zip");

      return ResponseEntity
              .ok()
              .headers(headers)
              .contentLength(resource.contentLength())
              .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
              .body(resource);

    } catch (IOException e) {
      throw new RuntimeException("Error creating ZIP file", e);
    }
  }
}
