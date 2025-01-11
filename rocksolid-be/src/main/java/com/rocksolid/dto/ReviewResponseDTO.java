package com.rocksolid.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ReviewResponseDTO {

  private Long reviewId;
  private List<ReviewDetailResponseDTO> reviewDetails;

}
