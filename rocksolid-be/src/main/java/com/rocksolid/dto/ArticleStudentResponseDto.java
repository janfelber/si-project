package com.rocksolid.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class ArticleStudentResponseDto {

  private String articleName;
  private String coAuthors;
  private String articleDescription;
  private String keyWords;
  private String section;
  private String firstName;
  private String lastName;
  private String status;

}
