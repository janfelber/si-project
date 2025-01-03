package com.rocksolid.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rocksolid.dto.NotificationResponseDto;
import com.rocksolid.module.Notification;
import com.rocksolid.repository.NotificationRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;

  @Override
  public List<NotificationResponseDto> getNotificationsForUser(Long userId) {
    return notificationRepository.findByUserId(userId)
        .stream()
        .filter(notification -> !notification.is_read())
        .map(notification -> new NotificationResponseDto(
            notification.getId(),
            notification.getMessage(),
            false,
            notification.getCreated_at()
        ))
        .collect(Collectors.toList());
  }

  @Override
  public void markNotificationAsRead(final Long notificationId) {
    Notification notification = notificationRepository.findById(notificationId).orElseThrow();
    notification.set_read(true);
    notificationRepository.save(notification);
  }

}
