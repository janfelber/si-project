package com.rocksolid.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rocksolid.dto.ReviewRequestDTO;
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

    System.out.println("Article ID: " + articleId);
    article.setStatus("ACCEPTED");
    reviewRepository.save(review);

    Map<Long, Long> columnValues = reviewRequestDto.getColumnValues();
    for (Map.Entry<Long, Long> entry : columnValues.entrySet()) {
      Long columnId = entry.getKey();
      Long value = entry.getValue();


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
  public Reviews getReviewByArticleId(final Long articleId) {
    // Načítaj recenziu na základe articleId
    Reviews review = reviewRepository.findByArticleId(articleId);
    if (review == null) {
      throw new RuntimeException("Review not found for article id: " + articleId);
    }

    List<ReviewDetails> reviewDetailsList = reviewDetailsRepository.findByReview(review);

    // Vypíš hodnoty do logu
    if (reviewDetailsList != null && !reviewDetailsList.isEmpty()) {
      System.out.println("Review Details for Article ID: " + articleId);
      for (ReviewDetails reviewDetail : reviewDetailsList) {
        Columns column = reviewDetail.getColumn_id();
        Choice choice = reviewDetail.getValue();
        System.out.println("Column: " + column.getColumn_name() + ", Value: " + choice.getChoice_name());
      }
    } else {
      System.out.println("No details found for Review with Article ID: " + articleId);
    }

    return review;
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
