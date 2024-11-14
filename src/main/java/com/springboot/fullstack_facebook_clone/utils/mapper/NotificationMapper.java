package com.springboot.fullstack_facebook_clone.utils.mapper;

import com.springboot.fullstack_facebook_clone.dto.model.NotificationModel;
import com.springboot.fullstack_facebook_clone.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotificationMapper {

    private final ModelMapper mapper = new ModelMapper();

    public NotificationModel mapEntityToModel(Notification notification) {
        return mapper.map(notification, NotificationModel.class);
    }

}
