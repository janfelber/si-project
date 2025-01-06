package com.rocksolid.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleAdminResponseDto {
  private Long id;
  private String articleName;
  private String coAuthors;
  private String articleDescription;
  private String keyWords;
  private String conferenceName;
  private String firstName;
  private String lastName;
  private Long userId;
  private String status;
}
