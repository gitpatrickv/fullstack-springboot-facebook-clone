package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.dto.model.NotificationModel;
import com.springboot.fullstack_facebook_clone.dto.response.NotificationResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PageResponse;
import com.springboot.fullstack_facebook_clone.entity.Notification;
import com.springboot.fullstack_facebook_clone.repository.NotificationRepository;
import com.springboot.fullstack_facebook_clone.service.NotificationService;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import com.springboot.fullstack_facebook_clone.utils.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public NotificationResponse fetchAllNotifications(Long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, StringUtil.TIMESTAMP));
        Page<Notification> notifications = notificationRepository.findAllByReceiver_UserId(userId, pageable);
        PageResponse pageResponse = this.getPagination(notifications);

        List<NotificationModel> notificationModels = new ArrayList<>();

        for(Notification notification : notifications){
            NotificationModel model = notificationMapper.mapEntityToModel(notification);
            model.setContent(notification.getPost().getContent());
            notificationModels.add(model);
        }

        return new NotificationResponse(notificationModels, pageResponse);
    }

    @Override
    public void markAsRead(Long notificationId) {
        Optional<Notification> notification = notificationRepository.findById(notificationId);

        notification.ifPresent(value -> value.setRead(true));
    }


    private PageResponse getPagination(Page<Notification> notifications){
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPageNo(notifications.getNumber());
        pageResponse.setPageSize(notifications.getSize());
        pageResponse.setTotalElements(notifications.getTotalElements());
        pageResponse.setTotalPages(notifications.getTotalPages());
        pageResponse.setLast(notifications.isLast());
        return pageResponse;
    }
}