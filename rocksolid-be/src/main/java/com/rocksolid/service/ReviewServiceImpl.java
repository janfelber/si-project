package com.rocksolid.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.rocksolid.dto.ReviewRequestDTO;
import com.rocksolid.module.Columns;
import com.rocksolid.module.ReviewDetails;
import com.rocksolid.module.Reviews;
import com.rocksolid.module.Article;
import com.rocksolid.repository.ArticleRepository;
import com.rocksolid.repository.ColumnRepository;
import com.rocksolid.repository.ReviewDeatailsRepository;
import com.rocksolid.repository.ReviewsRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

  private final ReviewsRepository reviewRepository;

  private final ReviewDeatailsRepository reviewDetailsRepository;

  private final ColumnRepository columnRepository;

  private final ArticleRepository articleRepository;

  @Override
  public Reviews createReview(ReviewRequestDTO reviewRequestDto) {
    Article article = articleRepository.findById(reviewRequestDto.getArticle_id())
        .orElseThrow(() -> new RuntimeException("Article not found"));

    Article customArticle = article.builder()
        .id(1L)  // Priradíš custom ID pre Article
        .build();
    Reviews review = Reviews.builder().article_id(customArticle).build();

    reviewRepository.save(review);

    Map<Long, String> columnValues = reviewRequestDto.getColumnValues();
    for (Map.Entry<Long, String> entry : columnValues.entrySet()) {
      Long columnId = entry.getKey();
      String value = entry.getValue();

      // Načítanie stĺpca na základe ID
      Columns column = columnRepository.findById(columnId)
          .orElseThrow(() -> new RuntimeException("Column not found"));

      // Vytvorenie a uloženie detailu recenzie (pre každý column_id)
      ReviewDetails reviewDetails = ReviewDetails.builder()
          .review_id(review) // Reference na recenziu
          .column_id(column)  // Reference na stĺpec
          .value(value)       // Hodnota recenzie pre tento stĺpec
          .build();

      reviewDetailsRepository.save(reviewDetails);
    }

    return review;
  }

}
