package com.rocksolid.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rocksolid.auth.CurrentUserService;
import com.rocksolid.module.article;
import com.rocksolid.service.ArticleService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/article")
public class ArticleController {

  private ArticleService articleService;
  private CurrentUserService currentUserService;

  @GetMapping("/status/{conferenceId}")
  public String getArticleStatus(@PathVariable Long conferenceId) {
    final Long currentUserId = currentUserService.getCurrentUserId();
    System.out.println("currentUserId: " + currentUserId);
    Optional<article> article = articleService.getArticleStatus(conferenceId, currentUserId);
    if (article.isPresent()) {
      final String status = article.get().getStatus();

      switch (status) {
        case "ACCEPTED":
          return "ACCEPTED";
        case "REJECTED":
          return "Article rejected";
        case "PENDING":
          return "Article pending";
        default:
          return "Article status unknown";
      }

    } else {
      return "Article not found";
    }
  }

}
