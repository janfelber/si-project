package com.rocksolid.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewRequestDTO {
  private Long article_id;
  private Map<Long, Long> columnValues;
  private Map<Long, String> textValues;

}
