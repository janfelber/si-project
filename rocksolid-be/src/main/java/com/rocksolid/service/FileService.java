package com.rocksolid.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.rocksolid.module.Article;

public interface FileService {
  Article saveFile(String firstName, String lastName, String fileName, String coAuthors, String articleDescription, String keyWords, Long sectionId, MultipartFile wordFile, MultipartFile pdfFile, Long conferenceId) throws IOException;
  byte[] loadFile(Long fileId) throws IOException;
  byte[] getFileByArticleId(Long articleId) throws IOException;
  String getFileName(Long articleId) throws IOException;
}
