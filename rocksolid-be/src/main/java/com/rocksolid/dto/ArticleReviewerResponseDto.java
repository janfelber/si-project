package com.rocksolid.dto;

import java.util.Date;

import com.rocksolid.module.Conference;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleReviewerResponseDto {
  private Long id;
  private String articleName;
  private String description;
  private String keywords;
  private String section;
  private Date createdAt;
  private String status;
  private String conferenceName;
}
