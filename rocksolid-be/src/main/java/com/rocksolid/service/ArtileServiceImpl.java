package com.rocksolid.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

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

}
