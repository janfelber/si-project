package com.rocksolid.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rocksolid.module.article;
import com.rocksolid.module.User;
import com.rocksolid.module.conference;
import com.rocksolid.repository.ConferenceRepository;
import com.rocksolid.repository.FileRepository;
import com.rocksolid.repository.UserRepository;

@Service
public class FileServiceImpl implements FileService{
  @Value("${file.storage.path}")
  private String fileStoragePath;

  private final FileRepository fileRepository;
  private final UserRepository userRepository;
  private final ConferenceRepository conferenceRepository;

  public FileServiceImpl(FileRepository fileRepository, final UserRepository userRepository, final ConferenceRepository conferenceRepository) {
    this.fileRepository = fileRepository;
    this.userRepository = userRepository;
    this.conferenceRepository = conferenceRepository;
  }

  @Override
  public article saveFile(
      String firstName,
      String lastName,
      String fileName,
      String coAuthors,
      String articleDescription,
      String keyWords,
      String section,
      MultipartFile file,
      Long conferenceId) throws IOException {

    Long userID = getCurrentUserId();
    User user = userRepository.findById(userID)
        .orElseThrow(() -> new RuntimeException("User not found"));

    conference conference = conferenceRepository.findById(conferenceId)
        .orElseThrow(() -> new RuntimeException("Conference not found"));


    String storedFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
    Path filePath = Paths.get(fileStoragePath, storedFileName);
    Files.createDirectories(filePath.getParent());
    Files.write(filePath, file.getBytes());

    article fileEntity = new article();
    fileEntity.setUser(user);
    fileEntity.setFirst_name(firstName);
    fileEntity.setLast_name(lastName);
    fileEntity.setArticle_name(fileName);
    fileEntity.setCo_authors(coAuthors);
    fileEntity.setArticle_description(articleDescription);
    fileEntity.setKey_words(keyWords);
    fileEntity.setSection(section);
    fileEntity.setFile_path(filePath.toString());
    fileEntity.setConference(conference);
    return fileRepository.save(fileEntity);
  }

  @Override
  public byte[] loadFile(Long fileId) throws IOException {
    article fileEntity = fileRepository.findById(Math.toIntExact(fileId))
        .orElseThrow(() -> new RuntimeException("File not found with id " + fileId));
    Path filePath = Paths.get(fileEntity.getFile_path());

    return Files.readAllBytes(filePath);
  }

  public Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new IllegalStateException("No authenticated user found");
    }

    Object principal = authentication.getPrincipal();
    if (principal instanceof UserDetails) {
      return ((User) principal).getId();
    } else {
      throw new IllegalStateException("Authentication principal is not an instance of UserDetails");
    }
  }

}
