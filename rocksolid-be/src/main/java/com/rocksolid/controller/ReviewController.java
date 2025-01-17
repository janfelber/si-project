package com.rocksolid.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rocksolid.dto.ReviewRequestDTO;
import com.rocksolid.dto.ReviewResponseDTO;
import com.rocksolid.module.Reviews;
import com.rocksolid.service.ReviewService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/review")
public class ReviewController {

  ReviewService reviewService;

  @PostMapping("/createReview")
  @PreAuthorize("hasAuthority('reviewer:create')")
  public Reviews createReview(@RequestBody ReviewRequestDTO reviewRequestDto) {
    return reviewService.createReview(reviewRequestDto);
  }

  @PostMapping("/rejectReview")
  @PreAuthorize("hasAuthority('reviewer:update')")
  public Reviews rejectReview(@RequestBody ReviewRequestDTO reviewRequestDto) {
    return reviewService.rejectReview(reviewRequestDto);
  }

  @GetMapping("/getReviewByArticleId/{articleId}")
  @PreAuthorize("hasAuthority('student:read') or hasAuthority('admin:read')")
  public ResponseEntity<ReviewResponseDTO> getReviewByArticleId(@PathVariable Long articleId) {
    return reviewService.getReviewByArticleId(articleId);
  }

  @DeleteMapping("/deleteReview/{id}")
  @PreAuthorize("hasAuthority('student:delete')")
  public Reviews deleteReview(@PathVariable Long id) {
    return reviewService.deleteReview(id);
  }

}
