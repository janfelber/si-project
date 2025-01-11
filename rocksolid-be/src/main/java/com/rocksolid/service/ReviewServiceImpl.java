package com.rocksolid.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rocksolid.dto.ReviewDetailResponseDTO;
import com.rocksolid.dto.ReviewRequestDTO;
import com.rocksolid.dto.ReviewResponseDTO;
import com.rocksolid.module.Choice;
import com.rocksolid.module.Columns;
import com.rocksolid.module.ReviewDetails;
import com.rocksolid.module.Reviews;
import com.rocksolid.module.Article;
import com.rocksolid.repository.ArticleRepository;
import com.rocksolid.repository.ChoiceRepository;
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
  private final ChoiceRepository choiceRepository;

  private final ArticleRepository articleRepository;

  @Override
  public Reviews createReview(ReviewRequestDTO reviewRequestDto) {
    Article article = articleRepository.findById(reviewRequestDto.getArticle_id())
        .orElseThrow(() -> new RuntimeException("Article not found"));

    Article articleId = article.builder()
        .id(article.getId())
        .build();
    Reviews review = Reviews.builder().article(articleId).build();


    article.setStatus("ACCEPTED");
    reviewRepository.save(review);

    Map<Long, Long> columnValues = reviewRequestDto.getColumnValues();


    // Iterácia cez výberové hodnoty (dropdown)
    for (Map.Entry<Long, Long> entry : columnValues.entrySet()) {
      Long columnId = entry.getKey();
      Long value = entry.getValue();

      // Načítanie stĺpca
      Columns column = columnRepository.findById(columnId)
          .orElseThrow(() -> new RuntimeException("Column not found"));

      // Načítanie voľby pre tento stĺpec
      Choice choice = choiceRepository.findById(value)
          .orElseThrow(() -> new RuntimeException("Choice not found"));



      // Vytvorenie a uloženie detailu recenzie pre daný stĺpec a voľbu
      ReviewDetails reviewDetails = ReviewDetails.builder()
          .review(review) // Reference na recenziu
          .column_id(column) // Reference na stĺpec
          .value(choice) // Hodnota recenzie pre tento stĺpec
          .build();

      reviewDetailsRepository.save(reviewDetails);
    }

    Map<Long, String> columnValuess = reviewRequestDto.getTextValues();
    for (Map.Entry<Long, String> entry : columnValuess.entrySet()) {
      Long columnId = entry.getKey();
      String value = entry.getValue();

      // Načítanie stĺpca na základe ID
      Columns column = columnRepository.findById(columnId)
          .orElseThrow(() -> new RuntimeException("Column not found"));

      // Vytvorenie a uloženie detailu recenzie (pre každý column_id)
      ReviewDetails reviewDetails = ReviewDetails.builder()
          .review(review) // Reference na recenziu
          .column_id(column)  // Reference na stĺpec
          .value(null)       // Hodnota recenzie pre tento stĺpec
          .text_value(value)       // Hodnota recenzie pre tento stĺpec
          .build();

      reviewDetailsRepository.save(reviewDetails);
    }

    return review;
  }

  public Reviews rejectReview(ReviewRequestDTO reviewRequestDto) {
    Article article = articleRepository.findById(reviewRequestDto.getArticle_id())
        .orElseThrow(() -> new RuntimeException("Article not found"));


    Article articleId = article.builder()
        .id(article.getId())
        .build();
    System.out.println("Article ID: " + articleId);
    Reviews review = Reviews.builder().article(articleId).build();

    article.setStatus("REJECTED");
    reviewRepository.save(review);

    Map<Long, Long> columnValues = reviewRequestDto.getColumnValues();
    for (Map.Entry<Long, Long> entry : columnValues.entrySet()) {
      Long columnId = entry.getKey();
      Long value = entry.getKey();


      Columns column = columnRepository.findById(columnId)
          .orElseThrow(() -> new RuntimeException("Column not found"));

      Choice choice = choiceRepository.findById(value)
          .orElseThrow(() -> new RuntimeException("Choice not found"));

      // Vytvorenie a uloženie detailu recenzie (pre každý column_id)
      ReviewDetails reviewDetails = ReviewDetails.builder()
          .review(review) // Reference na recenziu
          .column_id(column)  // Reference na stĺpec
          .value(choice)       // Hodnota recenzie pre tento stĺpec
          .build();


      reviewDetailsRepository.save(reviewDetails);
    }

    return review;
  }

  @Override
  public ResponseEntity<ReviewResponseDTO> getReviewByArticleId(final Long articleId) {
    Reviews review = reviewRepository.findByArticleId(articleId);
    if (review == null) {
      throw new RuntimeException("Review not found for article id: " + articleId);
    }

    List<ReviewDetails> reviewDetailsList = reviewDetailsRepository.findByReview(review);

    ReviewResponseDTO reviewResponse = new ReviewResponseDTO();
    reviewResponse.setReviewId(review.getId());
    reviewResponse.setReviewDetails(new ArrayList<>());

    if (reviewDetailsList != null && !reviewDetailsList.isEmpty()) {
      for (int i = 0; i < reviewDetailsList.size(); i++) {
        ReviewDetails reviewDetail = reviewDetailsList.get(i);
        Columns column = reviewDetail.getColumn_id();
        Choice choice = reviewDetail.getValue();
        String textValue = reviewDetail.getText_value();

        if (i < reviewDetailsList.size() - 2) {
          reviewResponse.getReviewDetails().add(new ReviewDetailResponseDTO(column.getColumn_name(), choice.getChoice_name(), textValue));
        } else {
          reviewResponse.getReviewDetails().add(new ReviewDetailResponseDTO(column.getColumn_name(), textValue, textValue));
        }
      }
    }

    return ResponseEntity.ok(reviewResponse);
  }


  // @Override
  // public Reviews getReviwByArticleId(final Long articleId) {
  //   return reviewRepository.findByArticleId(articleId);
  // }

  // public Map<String, String> getReviewColumns(Long reviewId) {
  //   // Načítanie recenzie podľa ID
  //   Reviews review = reviewRepository.findById(reviewId)
  //       .orElseThrow(() -> new RuntimeException("Review not found"));
  //
  //   // Získanie všetkých detailov recenzie (column_id a hodnoty)
  //   List<ReviewDetails> reviewDetailsList = reviewDetailsRepository.findByreview_id(review);
  //
  //   // Vytvorenie mapy stĺpcov a ich hodnôt
  //   Map<String, String> columnValues = new HashMap<>();
  //   for (ReviewDetails reviewDetails : reviewDetailsList) {
  //     Columns column = reviewDetails.getColumn_id(); // Získanie stĺpca
  //     String value = reviewDetails.getValue();       // Hodnota recenzie pre tento stĺpec
  //     columnValues.put(column.getColumn_name(), value);     // Pridanie do mapy
  //   }
  //
  //   return columnValues;
  // }

}
