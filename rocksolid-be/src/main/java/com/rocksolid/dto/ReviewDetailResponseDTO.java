package com.rocksolid.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewDetailResponseDTO {

  private String columnName;
  private String choiceName;
  private String textValue;
  private Long categoryId;
  private String categoryName;


}
