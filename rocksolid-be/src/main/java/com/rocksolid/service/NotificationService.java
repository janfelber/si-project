package com.rocksolid.service;

import java.util.List;

import com.rocksolid.dto.NotificationResponseDto;

public interface NotificationService {

  List<NotificationResponseDto> getNotificationsForUser(Long userId);

  void markNotificationAsRead(Long notificationId);
}
