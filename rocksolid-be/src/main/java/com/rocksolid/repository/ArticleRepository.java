package com.rocksolid.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rocksolid.module.article;

@Repository
public interface ArticleRepository extends JpaRepository<article, Long> {

  Optional<article> findByConferenceIdAndUserId(Long conferenceId, Long userId);

}
