package com.rocksolid.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rocksolid.module.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

  Optional<Article> findByConferenceIdAndUserId(Long conferenceId, Long userId);

  List<Article> findByReviewerId(Long reviewerId);

  List <Article> findByUserId(Long userId);

  boolean existsByIdAndConferenceId(Long articleId, Long conferenceId);
}
