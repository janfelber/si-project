package com.rocksolid.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rocksolid.dto.ArticleAdminUpdateRequest;
import com.rocksolid.dto.ArticleAdminResponseDto;
import com.rocksolid.dto.ArticleReviewerResponseDto;
import com.rocksolid.dto.ArticleStudentResponseDto;
import com.rocksolid.module.Article;
import com.rocksolid.module.Sections;
import com.rocksolid.module.User;
import com.rocksolid.repository.ArticleRepository;
import com.rocksolid.repository.SectionRepository;
import com.rocksolid.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

  private final ArticleRepository articleRepository;

  private final UserRepository userRepository;

  private final SectionRepository sectionRepository;

  @Override
  public Optional<Article> getArticleStatus(Long conferenceId, Long userId) {
    return articleRepository.findByConferenceIdAndUserId(conferenceId, userId);
  }

  @Override
  public List<ArticleAdminResponseDto> getAllArticlesInSystem() {
    return articleRepository.findAll()
        .stream()
        .map(article -> new ArticleAdminResponseDto(
            article.getId(),
            article.getArticle_name(),
            article.getCo_authors(),
            article.getArticle_description(),
            article.getKey_words(),
            article.getConference().getName(),
            article.getSections().getName(),
            article.getFirst_name(),
            article.getLast_name(),
            article.getUser().getId(),
            article.getStatus(),
            article.getReviewer() == null ? null : article.getReviewer().getId(),
            article.getReviewer() == null ? null : article.getReviewer().getFirst_name(),
            article.getReviewer() == null ? null : article.getReviewer().getLast_name(),
            article.isReviewed()
        )).collect(Collectors.toList());
  }

  @Override
  public ArticleAdminResponseDto getArticleById(final Long id) {
    return articleRepository.findById(id)
        .map(article -> new ArticleAdminResponseDto(
            article.getId(),
            article.getArticle_name(),
            article.getCo_authors(),
            article.getArticle_description(),
            article.getKey_words(),
            article.getConference().getName(),
            article.getSections().getName(),
            article.getFirst_name(),
            article.getLast_name(),
            article.getUser().getId(),
            article.getStatus(),
            article.getReviewer() == null ? null : article.getReviewer().getId(),
            article.getReviewer() == null ? null : article.getReviewer().getFirst_name(),
            article.getReviewer() == null ? null : article.getReviewer().getLast_name(),
            article.isReviewed()
        )).orElseThrow();
  }

  @Override
  public ArticleStudentResponseDto getArticleByIdStudent(final Long id) {
    return articleRepository.findById(id).map(article -> new ArticleStudentResponseDto(
        article.getArticle_name(),
        article.getCo_authors(),
        article.getArticle_description(),
        article.getKey_words(),
        article.getSections().getName(),
        article.getFirst_name(),
        article.getLast_name(),
        article.getStatus(),
        article.getConference().getId()
    )).orElseThrow();
  }

  @Override
  public Article adminUpdateArticle(final Long articleId, final ArticleAdminUpdateRequest updateArticleRequest) {
    final Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new RuntimeException("Article not found"));

    if (updateArticleRequest.getReviewerId() != null) {
      final User reviewer = userRepository.findById(updateArticleRequest.getReviewerId())
          .orElseThrow(() -> new RuntimeException("Reviewer not found"));
      article.setReviewer(reviewer);
    }

    if (updateArticleRequest.getSectionId() != null) {
      final Sections section = sectionRepository.findById(updateArticleRequest.getSectionId())
          .orElseThrow(() -> new RuntimeException("Section not found"));
      article.setSections(section);
    }

    return articleRepository.save(article);
  }

  @Override
  public List<ArticleReviewerResponseDto> getArticlesAssignedToReviewer(final Long reviewerId) {
    userRepository.findById(reviewerId)
        .orElseThrow(() -> new RuntimeException("Reviewer not found"));

    return articleRepository.findByReviewerId(reviewerId)
        .stream()
        .filter(article -> !article.isReviewed())
        .map(article -> new ArticleReviewerResponseDto(
            article.getId(),
            article.getArticle_name(),
            article.getArticle_description(),
            article.getKey_words(),
            article.getSections().getName(),
            article.getCreated_at(),
            article.getStatus(),
            article.getConference().getName()
        )).collect(Collectors.toList());
  }

  @Override
  public List<ArticleReviewerResponseDto> getArticlesByUserId(Long userId){
    userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    return articleRepository.findByUserId(userId)
            .stream()
            .map(article -> new ArticleReviewerResponseDto(
                    article.getId(),
                    article.getArticle_name(),
                    article.getArticle_description(),
                    article.getKey_words(),
                    article.getSections().getName(),
                    article.getCreated_at(),
                    article.getStatus(),
                    article.getConference().getName()
            )).collect(Collectors.toList());
  }
}
