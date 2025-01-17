package com.rocksolid.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rocksolid.dto.ReviewDetailResponseDTO;
import com.rocksolid.dto.ReviewRequestDTO;
import com.rocksolid.dto.ReviewResponseDTO;
import com.rocksolid.module.Choice;
import com.rocksolid.module.Columns;
import com.rocksolid.module.Notification;
import com.rocksolid.module.ReviewDetails;
import com.rocksolid.module.Reviews;
import com.rocksolid.module.Article;
import com.rocksolid.module.User;
import com.rocksolid.repository.ArticleRepository;
import com.rocksolid.repository.ChoiceRepository;
import com.rocksolid.repository.ColumnRepository;
import com.rocksolid.repository.NotificationRepository;
import com.rocksolid.repository.ReviewDeatailsRepository;
import com.rocksolid.repository.ReviewsRepository;
import com.rocksolid.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

  private final ReviewsRepository reviewRepository;

  private final ReviewDeatailsRepository reviewDetailsRepository;

  private final ColumnRepository columnRepository;
  private final ChoiceRepository choiceRepository;

  private final ArticleRepository articleRepository;
  private final NotificationRepository notificationRepository;

  private final UserRepository userRepository;

  @Override
  public Reviews createReview(final ReviewRequestDTO reviewRequestDto) {
    final Article article = articleRepository.findById(reviewRequestDto.getArticle_id())
        .orElseThrow(() -> new RuntimeException("Article not found"));

    final Article articleId = Article.builder()
        .id(article.getId())
        .build();
    Reviews review = Reviews.builder().article(articleId).build();


    article.setStatus("ACCEPTED");
    article.setReviewed(true);
    reviewRepository.save(review);

    Map<Long, Long> columnValues = reviewRequestDto.getColumnValues();



    for (Map.Entry<Long, Long> entry : columnValues.entrySet()) {
      Long columnId = entry.getKey();
      Long value = entry.getValue();


      Columns column = columnRepository.findById(columnId)
          .orElseThrow(() -> new RuntimeException("Column not found"));

      Choice choice = choiceRepository.findById(value)
          .orElseThrow(() -> new RuntimeException("Choice not found"));

      ReviewDetails reviewDetails = ReviewDetails.builder()
          .review(review)
          .column_id(column)
          .value(choice)
          .build();

      reviewDetailsRepository.save(reviewDetails);
    }

    Map<Long, String> columnValuess = reviewRequestDto.getTextValues();
    for (Map.Entry<Long, String> entry : columnValuess.entrySet()) {
      Long columnId = entry.getKey();
      String value = entry.getValue();

      Columns column = columnRepository.findById(columnId)
          .orElseThrow(() -> new RuntimeException("Column not found"));

      ReviewDetails reviewDetails = ReviewDetails.builder()
          .review(review)
          .column_id(column)
          .value(null)
          .text_value(value)
          .build();

      reviewDetailsRepository.save(reviewDetails);
    }

    createNotification(article.getUser(), "Článok s názvom " + article.getArticle_name() + " bol prijatý pre konferenciu " + article.getConference().getName());

    return review;
  }

  public Reviews rejectReview(ReviewRequestDTO reviewRequestDto) {
    final Article article = articleRepository.findById(reviewRequestDto.getArticle_id())
        .orElseThrow(() -> new RuntimeException("Article not found"));

    final Article articleId = article.builder()
        .id(article.getId())
        .build();
    Reviews review = Reviews.builder().article(articleId).build();

    article.setStatus("REJECTED");
    article.setReviewed(true);
    reviewRepository.save(review);

    Map<Long, Long> columnValues = reviewRequestDto.getColumnValues();

    for (Map.Entry<Long, Long> entry : columnValues.entrySet()) {
      Long columnId = entry.getKey();
      Long value = entry.getValue();


      Columns column = columnRepository.findById(columnId)
          .orElseThrow(() -> new RuntimeException("Column not found"));

      Choice choice = choiceRepository.findById(value)
          .orElseThrow(() -> new RuntimeException("Choice not found"));

      ReviewDetails reviewDetails = ReviewDetails.builder()
          .review(review)
          .column_id(column)
          .value(choice)
          .build();

      reviewDetailsRepository.save(reviewDetails);
    }

    Map<Long, String> columnValuess = reviewRequestDto.getTextValues();
    for (Map.Entry<Long, String> entry : columnValuess.entrySet()) {
      Long columnId = entry.getKey();
      String value = entry.getValue();

      Columns column = columnRepository.findById(columnId)
          .orElseThrow(() -> new RuntimeException("Column not found"));

      ReviewDetails reviewDetails = ReviewDetails.builder()
          .review(review)
          .column_id(column)
          .value(null)
          .text_value(value)
          .build();

      reviewDetailsRepository.save(reviewDetails);
    }

    createNotification(article.getUser(), "Článok s názvom " + article.getArticle_name() + " bol zamietnutý pre konferenciu " + article.getConference().getName() + " - článok je potrebné upraviť");

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
          reviewResponse.getReviewDetails().add(new ReviewDetailResponseDTO(column.getColumn_name(),choice.getChoice_name(), textValue, column.getCategory().getId(),column.getCategory().getCategory_name()));
        } else {
          reviewResponse.getReviewDetails().add(new ReviewDetailResponseDTO(column.getColumn_name(), textValue, textValue, column.getCategory().getId(),column.getCategory().getCategory_name()));
        }
      }
    }

    return ResponseEntity.ok(reviewResponse);
  }

  @Override
  public Reviews deleteReview(final Long id) {
    // Reviews review = reviewRepository.findByArticleId(id);
    // reviewDetailsRepository.deleteReviewDetailsByReviewId(id);
    // reviewRepository.deleteById(id);


    Reviews review = reviewRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Review not found"));

    List<ReviewDetails> reviewDetailsList = reviewDetailsRepository.findByReview(review);
    if (reviewDetailsList != null && !reviewDetailsList.isEmpty()) {
      // Vymazanie všetkých detailov recenzie
      reviewDetailsRepository.deleteAll(reviewDetailsList);
    }

    reviewRepository.deleteById(id);

    System.out.println("Review deleted with id: " + id);

    return review;
  }

  private void createNotification(User user, String message) {
    Notification notification = Notification.builder()
        .user(user)
        .message(message)
        .is_read(false)
        .created_at(new Date())
        .build();

    notificationRepository.save(notification);
  }
}
