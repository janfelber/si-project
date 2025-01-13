package com.rocksolid.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rocksolid.module.ReviewDetails;
import com.rocksolid.module.Reviews;

public interface ReviewDeatailsRepository extends JpaRepository<ReviewDetails, Long> {

  List<ReviewDetails> findByReviewId(Long reviewId);
  List<ReviewDetails> findByReview(Reviews review);
  List<ReviewDetails> deleteReviewDetailsByReviewId(Long reviewid);


}
