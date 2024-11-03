package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.request.SendMessageRequest;
import com.springboot.fullstack_facebook_clone.dto.response.MessageResponse;

public interface MessageService {

    void sendMessage(SendMessageRequest request);
    MessageResponse fetchAllChatMessages (Long chatId, int pageNo, int pageSize);
}
