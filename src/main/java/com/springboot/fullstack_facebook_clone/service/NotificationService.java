package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.model.NotificationModel;
import com.springboot.fullstack_facebook_clone.dto.response.CountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.NotificationResponse;

public interface NotificationService {

    NotificationResponse fetchAllNotifications(Long userId, int pageNo, int pageSize);
    void markAsRead(Long notificationId);
    CountResponse getNotificationCount(Long userId);
    void sendNotification(String email, NotificationModel notificationModel);
}
