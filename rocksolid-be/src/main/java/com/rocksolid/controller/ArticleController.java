package com.rocksolid.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rocksolid.auth.CurrentUserService;
import com.rocksolid.dto.AdminArticleUpdateRequest;
import com.rocksolid.dto.ArticleAdminResponseDto;
import com.rocksolid.dto.SectionResponseDTO;
import com.rocksolid.module.Article;
import com.rocksolid.service.ArticleService;
import com.rocksolid.service.SectionService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/article")
public class ArticleController {

  private ArticleService articleService;
  private CurrentUserService currentUserService;
  private SectionService sectionService;

  @GetMapping("/sections")
  public List<SectionResponseDTO> getAllSections() {
    return sectionService.getAllSectionNames();
  }

  @GetMapping("/status/{conferenceId}")
  public String getArticleStatus(@PathVariable Long conferenceId) {
    final Long currentUserId = currentUserService.getCurrentUserId();
    System.out.println("currentUserId: " + currentUserId);
    Optional<Article> article = articleService.getArticleStatus(conferenceId, currentUserId);
    if (article.isPresent()) {
      final String status = article.get().getStatus();

      switch (status) {
        case "SENT":
          return "SENT";
        case "REJECTED":
          return "REJECTED";
        case "ACCEPTED":
          return "ACCEPTED";
        default:
          return "Article status unknown";
      }

    } else {
      return "Article not found";
    }
  }


  @GetMapping("/all")
  @PreAuthorize("hasAuthority('admin:read')")
  public Iterable<ArticleAdminResponseDto> getAllArticles() {
    return articleService.getAllArticlesInSystem();
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('admin:read')")
  public ArticleAdminResponseDto getArticleById(@PathVariable final Long id) {
    return articleService.getArticleById(id);
  }

  @PutMapping("/admin/update/{articleId}")
  @PreAuthorize("hasAuthority('admin:update')")
  public Article adminUpdateArticle(@PathVariable final Long articleId, @RequestBody final AdminArticleUpdateRequest updateArticleRequest) {
    return articleService.adminUpdateArticle(articleId, updateArticleRequest);
  }

}
