package com.rocksolid.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rocksolid.auth.CurrentUserService;
import com.rocksolid.dto.NotificationResponseDto;
import com.rocksolid.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {

  private final NotificationService notificationService;
  private final CurrentUserService currentUserService;

  @GetMapping("/get/unread")
  public List<NotificationResponseDto> getUnreadNotifications() {
    final Long userId = currentUserService.getCurrentUserId();
    System.out.println("userId: " + userId);
    return notificationService.getNotificationsForUser(userId);
  }

  @PutMapping("/mark/read/{notificationId}")
  public void markNotificationAsRead(@PathVariable final Long notificationId) {
    notificationService.markNotificationAsRead(notificationId);
  }


}
