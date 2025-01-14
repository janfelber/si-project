package com.rocksolid.service;

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
  public byte[] loadFile(Long fileId) throws IOException {
    Article fileEntity = fileRepository.findById(Math.toIntExact(fileId))
        .orElseThrow(() -> new RuntimeException("File not found with id " + fileId));
    Path filePath = Paths.get(fileEntity.getWord_file_path());

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

  @Override
  public byte[] getFileByArticleId(Long articleId) throws IOException{
    Optional<Article> article = articleRepository.findById(articleId);
    if (article.isEmpty()) {
      throw new RuntimeException("Article not found with ID: " + articleId);
    }
    String filePath = article.get().getWord_file_path();
    String pdfFilePath = article.get().getPdf_file_path();
    Path path = Paths.get(filePath);
    return Files.readAllBytes(path);
  }

  public String getFileName(Long articleId){
    Optional<Article> article = articleRepository.findById(articleId);
    if (article.isEmpty()) {
      throw new RuntimeException("Article not found with ID: " + articleId);
    }
    String path = article.get().getWord_file_path();
    String fullFileName = path.substring(path.lastIndexOf("\\"));
    return fullFileName.substring(fullFileName.indexOf("_") + 1);
  }


}
