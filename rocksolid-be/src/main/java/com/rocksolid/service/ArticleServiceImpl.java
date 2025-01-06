package com.rocksolid.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rocksolid.dto.AdminArticleUpdateRequest;
import com.rocksolid.dto.ArticleAdminResponseDto;
import com.rocksolid.module.Article;
import com.rocksolid.module.User;
import com.rocksolid.repository.ArticleRepository;
import com.rocksolid.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

  private final ArticleRepository articleRepository;

  private final UserRepository userRepository;

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
            // article.getSection(),
            article.getFirst_name(),
            article.getLast_name(),
            article.getUser().getId(),
            article.getStatus(),
            article.getReviewer() == null ? null : article.getReviewer().getId()
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
            // article.getSection(),
            article.getFirst_name(),
            article.getLast_name(),
            article.getUser().getId(),
            article.getStatus(),
            article.getReviewer() == null ? null : article.getReviewer().getId()
        )).orElseThrow();
  }

  @Override
  public Article adminUpdateArticle(final Long articleId, final AdminArticleUpdateRequest updateArticleRequest) {
    final Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new RuntimeException("Article not found"));

    final Long reviewerId = updateArticleRequest.getReviewerId();
    final User reviewer = userRepository.findById(reviewerId)
        .orElseThrow(() -> new RuntimeException("Reviewer not found"));

    article.setReviewer(reviewer);

    return articleRepository.save(article);
  }

}
