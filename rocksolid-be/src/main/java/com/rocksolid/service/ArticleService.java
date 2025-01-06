package com.rocksolid.service;

import java.util.List;
import java.util.Optional;

import com.rocksolid.dto.AdminArticleUpdateRequest;
import com.rocksolid.dto.ArticleAdminResponseDto;
import com.rocksolid.module.Article;

public interface ArticleService {
  Optional<Article> getArticleStatus(Long conferenceId, Long userId);

  List<ArticleAdminResponseDto> getAllArticlesInSystem();

  ArticleAdminResponseDto getArticleById(Long id);

  Article adminUpdateArticle(Long articleId, AdminArticleUpdateRequest updateArticleRequest);
}
