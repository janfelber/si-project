package com.rocksolid.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewerAdminResponseDto {
  private Long id;
  private String firstName;
  private String lastName;
}
