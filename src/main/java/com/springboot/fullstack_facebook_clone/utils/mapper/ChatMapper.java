package com.springboot.fullstack_facebook_clone.utils.mapper;

import com.springboot.fullstack_facebook_clone.dto.model.ChatModel;
import com.springboot.fullstack_facebook_clone.entity.Chat;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChatMapper {

    private final ModelMapper mapper = new ModelMapper();

    public ChatModel mapEntityToModel(Chat chat){
        return mapper.map(chat, ChatModel.class);
    }
}
