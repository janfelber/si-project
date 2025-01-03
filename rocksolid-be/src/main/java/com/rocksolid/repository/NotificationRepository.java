package com.rocksolid.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rocksolid.module.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
  List<Notification> findByUserId(Long userId);
}
