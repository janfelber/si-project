package com.rocksolid.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rocksolid.dto.ReviewRequestDTO;
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
  public Reviews createReview(@RequestBody ReviewRequestDTO reviewRequestDto) {
    return reviewService.createReview(reviewRequestDto);
  }

}
