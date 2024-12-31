package com.rocksolid.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rocksolid.module.article;

public interface FileService {
  article saveFile(MultipartFile file, String fileName,String coAuthors,String articleDescription,String keyWords) throws IOException;
  byte[] loadFile(Long fileId) throws IOException;

}
