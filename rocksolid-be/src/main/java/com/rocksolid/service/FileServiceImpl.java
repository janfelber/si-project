package com.rocksolid.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rocksolid.module.Sections;
import com.rocksolid.module.Article;
import com.rocksolid.module.User;
import com.rocksolid.module.conference;
import com.rocksolid.repository.ConferenceRepository;
import com.rocksolid.repository.FileRepository;
import com.rocksolid.repository.SectionRepository;
import com.rocksolid.repository.UserRepository;

@Service
public class FileServiceImpl implements FileService{
  @Value("${file.storage.path}")
  private String fileStoragePath;

  private final FileRepository fileRepository;
  private final UserRepository userRepository;
  private final ConferenceRepository conferenceRepository;
  private final SectionRepository sectionRepository;

  public FileServiceImpl(FileRepository fileRepository, final UserRepository userRepository, final ConferenceRepository conferenceRepository,
      final SectionRepository sectionRepository) {
    this.fileRepository = fileRepository;
    this.userRepository = userRepository;
    this.conferenceRepository = conferenceRepository;
    this.sectionRepository = sectionRepository;
  }

  @Override
  public Article saveFile(
      String firstName,
      String lastName,
      String fileName,
      String coAuthors,
      String articleDescription,
      String keyWords,
      Long sectionId,
      MultipartFile file,
      Long conferenceId) throws IOException {

    Long userID = getCurrentUserId();
    User user = userRepository.findById(userID)
        .orElseThrow(() -> new RuntimeException("User not found"));

    conference conference = conferenceRepository.findById(conferenceId)
        .orElseThrow(() -> new RuntimeException("Conference not found"));

    Sections section = sectionRepository.findById(sectionId)
        .orElseThrow(() -> new RuntimeException("Section not found"));

    String storedFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
    Path filePath = Paths.get(fileStoragePath, storedFileName);
    Files.createDirectories(filePath.getParent());
    Files.write(filePath, file.getBytes());

    Article fileEntity = new Article();
    fileEntity.setUser(user);
    fileEntity.setFirst_name(firstName);
    fileEntity.setLast_name(lastName);
    fileEntity.setArticle_name(fileName);
    fileEntity.setCo_authors(coAuthors);
    fileEntity.setArticle_description(articleDescription);
    fileEntity.setKey_words(keyWords);
    fileEntity.setSections(section);
    fileEntity.setFile_path(filePath.toString());
    fileEntity.setConference(conference);
    fileEntity.setStatus("SENT");
    fileEntity.setCreated_at(new Date());
    return fileRepository.save(fileEntity);
  }

  @Override
  public byte[] loadFile(Long fileId) throws IOException {
    Article fileEntity = fileRepository.findById(Math.toIntExact(fileId))
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
