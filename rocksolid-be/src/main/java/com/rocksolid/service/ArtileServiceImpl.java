package com.rocksolid.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rocksolid.dto.ArticleAdminResponseDto;
import com.rocksolid.module.article;
import com.rocksolid.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArtileServiceImpl implements ArticleService {

  private final ArticleRepository articleRepository;

  @Override
  public Optional<article> getArticleStatus(Long conferenceId, Long userId) {
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
            article.getSection(),
            article.getFirst_name(),
            article.getLast_name(),
            article.getUser().getId(),
            article.getStatus()
        )).collect(Collectors.toList());
  }

}
