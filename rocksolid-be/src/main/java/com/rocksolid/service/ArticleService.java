package com.rocksolid.service;

import java.util.Optional;

import com.rocksolid.module.article;

public interface ArticleService {
  Optional<article> getArticleStatus(Long conferenceId, Long userId);

}
