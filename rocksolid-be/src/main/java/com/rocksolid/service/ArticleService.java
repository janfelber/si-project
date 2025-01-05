package com.rocksolid.service;

import java.util.List;
import java.util.Optional;

import com.rocksolid.dto.ArticleAdminResponseDto;
import com.rocksolid.module.article;

public interface ArticleService {
  Optional<article> getArticleStatus(Long conferenceId, Long userId);

  List<ArticleAdminResponseDto> getAllArticlesInSystem();

  ArticleAdminResponseDto getArticleById(Long id);

}
