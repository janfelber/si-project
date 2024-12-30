package com.rocksolid.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rocksolid.module.article;
import com.rocksolid.repository.FileRepository;

@Service
public class FileServiceImpl implements FileService {


  private final FileRepository fileRepository;

  public FileServiceImpl(final FileRepository fileRepository) {
    this.fileRepository = fileRepository;
  }

  public String saveFile(MultipartFile file) throws IOException {
    byte[] fileBytes = file.getBytes();
    System.out.println("File bytes length: " + fileBytes.length);
    System.out.println("File content type: " + file.getContentType());

    if (fileBytes instanceof byte[]) {
      System.out.println("fileBytes is a byte array");
    } else {
      System.out.println("fileBytes is NOT a byte array");
    }
    article files = article
        .builder()
        .article_name(file.getOriginalFilename())
        .file(fileBytes)
        .build();

    System.out.println("Original filename: " + file.getOriginalFilename());

    files = fileRepository.save(files);



    if (files.getId() != 0) {
      return "File uploaded succesufully";
    }

    return null;
  }

  @Override
  public byte[] getFile(final String fileName) {
    return new byte[0];
  }

  // public byte[] getFile(String fileName) {
  //   return fileRepository.findByarticle_name(fileName).getFile();
  // }


}
