package com.rocksolid.service;

import com.rocksolid.dto.ReviewRequestDTO;
import com.rocksolid.module.Reviews;

public interface ReviewService {

  Reviews createReview(ReviewRequestDTO reviewRequestDto);
  Reviews rejectReview(ReviewRequestDTO reviewRequestDto);

}
