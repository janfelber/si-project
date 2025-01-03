package com.rocksolid.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationResponseDto {
  private Long id;
  private String message;
  private boolean isRead;
  private Date createdAt;
}
