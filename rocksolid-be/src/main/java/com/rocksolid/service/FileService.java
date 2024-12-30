package com.rocksolid.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
  String saveFile(MultipartFile file) throws IOException;
  byte[] getFile(String fileName) ;

}
