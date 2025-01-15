package com.rocksolid.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Date;
import java.util.UUID;

import com.rocksolid.repository.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rocksolid.module.Sections;
import com.rocksolid.module.Article;
import com.rocksolid.module.User;
import com.rocksolid.module.Conference;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

  private final ArticleRepository articleRepository;

  @Value("${file.storage.path}")
  private String fileStoragePath;

  private final FileRepository fileRepository;

  private final UserRepository userRepository;

  private final ConferenceRepository conferenceRepository;

  private final SectionRepository sectionRepository;

  @Override
  public Article saveFile(
      String firstName,
      String lastName,
      String fileName,
      String coAuthors,
      String articleDescription,
      String keyWords,
      Long sectionId,
      MultipartFile wordFile,
      MultipartFile pdfFile,
      Long conferenceId) throws IOException {

    Long userID = getCurrentUserId();
    User user = userRepository.findById(userID)
        .orElseThrow(() -> new RuntimeException("User not found"));

    Conference conference = conferenceRepository.findById(conferenceId)
        .orElseThrow(() -> new RuntimeException("Conference not found"));

    Sections section = sectionRepository.findById(sectionId)
        .orElseThrow(() -> new RuntimeException("Section not found"));

    String storedFileName = UUID.randomUUID() + "_" + wordFile.getOriginalFilename();
    Path filePath = Paths.get(fileStoragePath, storedFileName);

    String pdfFileName = UUID.randomUUID() + "_" + pdfFile.getOriginalFilename();
    Path pdfFilePath = Paths.get(fileStoragePath, pdfFileName);
    Files.createDirectories(filePath.getParent());
    Files.write(filePath, wordFile.getBytes());
    Files.write(pdfFilePath, pdfFile.getBytes());

    Article fileEntity = new Article();
    fileEntity.setUser(user);
    fileEntity.setFirst_name(firstName);
    fileEntity.setLast_name(lastName);
    fileEntity.setArticle_name(fileName);
    fileEntity.setCo_authors(coAuthors);
    fileEntity.setArticle_description(articleDescription);
    fileEntity.setKey_words(keyWords);
    fileEntity.setSections(section);
    fileEntity.setWord_file_path(filePath.toString());
    fileEntity.setPdf_file_path(pdfFilePath.toString());
    fileEntity.setConference(conference);
    fileEntity.setStatus("SENT");
    fileEntity.setCreated_at(new Date());
    return fileRepository.save(fileEntity);
  }

  @Override
  public Article updateFile(final String firstName, final String lastName, final String fileName,
      final String coAuthors, final String articleDescription, final String keyWords, final Long sectionId,
      final MultipartFile wordFile, final MultipartFile pdfFile, final Long conferenceId, final Long articleId)
      throws IOException {

    Article fileEntity = articleRepository.findById(articleId)
        .orElseThrow(() -> new RuntimeException("File not found with id " + articleId));

    if (firstName != null) {
      fileEntity.setFirst_name(firstName);
    }
    if (lastName != null) {
      fileEntity.setLast_name(lastName);
    }
    if (fileName != null) {
      fileEntity.setArticle_name(fileName);
    }
    if (coAuthors != null) {
      fileEntity.setCo_authors(coAuthors);
    }
    if (articleDescription != null) {
      fileEntity.setArticle_description(articleDescription);
    }
    if (keyWords != null) {
      fileEntity.setKey_words(keyWords);
    }

    if (sectionId != null) {
      fileEntity.setSections(sectionRepository.findById(sectionId)
          .orElseThrow(() -> new RuntimeException("Section not found")));
    }

    if (conferenceId != null) {
      fileEntity.setConference(conferenceRepository.findById(conferenceId)
          .orElseThrow(() -> new RuntimeException("Conference not found")));
    }

    if (wordFile != null && !wordFile.isEmpty()) {
      String storedFileName = UUID.randomUUID() + "_" + wordFile.getOriginalFilename();
      Path filePath = Paths.get(fileStoragePath, storedFileName);
      Files.createDirectories(filePath.getParent());
      Files.write(filePath, wordFile.getBytes());
      fileEntity.setWord_file_path(filePath.toString());
    }

    if (pdfFile != null && !pdfFile.isEmpty()) {
      String pdfFileName = UUID.randomUUID() + "_" + pdfFile.getOriginalFilename();
      Path pdfFilePath = Paths.get(fileStoragePath, pdfFileName);
      Files.createDirectories(pdfFilePath.getParent());
      Files.write(pdfFilePath, pdfFile.getBytes());
      fileEntity.setPdf_file_path(pdfFilePath.toString());
    }

    fileEntity.setStatus("SENT");
    fileEntity.setReviewed(false);

    return articleRepository.save(fileEntity);
  }

  @Override
  public byte[] loadFile(final Long articleId, final String fileType) throws IOException {
    final Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new RuntimeException("Article not found with ID: " + articleId));

    final String filePath = switch (fileType) {
      case "pdf" -> article.getPdf_file_path();
      case "word" -> article.getWord_file_path();
      default -> throw new IllegalArgumentException("Unsupported file type: " + fileType);
    };

    final Path path = Paths.get(filePath);
    return Files.readAllBytes(path);
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

  public String getFileName(final Long articleId, final String fileType) {
    final Optional<Article> article = articleRepository.findById(articleId);
    if (article.isEmpty()) {
      throw new RuntimeException("Article not found with ID: " + articleId);
    }

    final String filePath = switch (fileType) {
      case "pdf" -> article.get().getPdf_file_path();
      case "word" -> article.get().getWord_file_path();
      default -> throw new IllegalArgumentException("Unsupported file type: " + fileType);
    };

    String fullFileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);

    if (fullFileName.contains("_")) {
      fullFileName = fullFileName.substring(fullFileName.indexOf('_') + 1);
    }
    return fullFileName;
  }


}
