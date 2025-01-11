package com.rocksolid.service;

import org.springframework.http.ResponseEntity;

import com.rocksolid.dto.ReviewRequestDTO;
import com.rocksolid.dto.ReviewResponseDTO;
import com.rocksolid.module.Reviews;

public interface ReviewService {

  Reviews createReview(ReviewRequestDTO reviewRequestDto);
  Reviews rejectReview(ReviewRequestDTO reviewRequestDto);
  ResponseEntity<ReviewResponseDTO> getReviewByArticleId(Long articleId);

}
