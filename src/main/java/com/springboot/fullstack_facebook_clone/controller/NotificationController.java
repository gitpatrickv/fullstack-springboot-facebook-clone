package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.dto.response.CountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.NotificationResponse;
import com.springboot.fullstack_facebook_clone.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    @GetMapping("/{userId}")
    public NotificationResponse fetchAllNotifications(@PathVariable("userId") Long userId,
                                                     @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                     @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return notificationService.fetchAllNotifications(userId,pageNo,pageSize);
    }
    @PostMapping("/{notificationId}")
    public void markAsRead(@PathVariable("notificationId") Long notificationId) {
        notificationService.markAsRead(notificationId);
    }
    @GetMapping("/count/{userId}")
    public CountResponse getNotificationCount(@PathVariable("userId") Long userId) {
        return notificationService.getNotificationCount(userId);
    }
    @DeleteMapping("/{notificationId}")
    public void deleteNotification(@PathVariable("notificationId") Long notificationId) {
        notificationService.deleteNotification(notificationId);
    }

}
