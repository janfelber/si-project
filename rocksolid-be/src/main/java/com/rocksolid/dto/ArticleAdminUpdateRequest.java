package com.rocksolid.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleAdminUpdateRequest {
  private Long reviewerId;
  private Long sectionId;
}
