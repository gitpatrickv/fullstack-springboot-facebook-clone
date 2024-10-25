package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.response.NotificationResponse;

public interface NotificationService {

    NotificationResponse fetchAllNotifications(Long userId, int pageNo, int pageSize);
}
