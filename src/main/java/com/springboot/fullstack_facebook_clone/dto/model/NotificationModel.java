package com.springboot.fullstack_facebook_clone.dto.model;

import com.springboot.fullstack_facebook_clone.entity.constants.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationModel{

    private Long notificationId;
    private String message;
    private boolean isRead;
    private String timestamp;
    private NotificationType notificationType;
    private Long receiverId;
    private UserDataModel sender;
    private Long postId;
    private String content;
}
