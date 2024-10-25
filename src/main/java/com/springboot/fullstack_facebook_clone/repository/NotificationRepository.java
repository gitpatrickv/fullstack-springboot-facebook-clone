package com.springboot.fullstack_facebook_clone.repository;

import com.springboot.fullstack_facebook_clone.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    void deleteByPost_PostIdAndSender_UserId(Long postId, Long userId);
    Page<Notification> findAllByReceiver_UserId(Long userId, Pageable pageable);
}
