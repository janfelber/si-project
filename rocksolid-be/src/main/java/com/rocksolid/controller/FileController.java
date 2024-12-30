package com.rocksolid.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.rocksolid.service.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
@CrossOrigin(origins = "http://localhost:3000")
public class FileController {

  private FileService fileService;

  @Autowired
  public FileController(final FileService fileService) {
    this.fileService = fileService;
  }

  @PostMapping("/upload")
  public ResponseEntity<String> saveFileToDB(@RequestParam("file") MultipartFile file) throws IOException {
    String result = fileService.saveFile(file);
    if (result != null) {
      return ResponseEntity.status(HttpStatus.OK).body(result);  // 200 OK
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
  }

  @GetMapping("/getFileByName/{fileName}")
  public ResponseEntity<byte[]> getFile(@PathVariable String fileName) {
    byte[] file = fileService.getFile(fileName);

    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("docx/doc")).body(file);
  }

}
