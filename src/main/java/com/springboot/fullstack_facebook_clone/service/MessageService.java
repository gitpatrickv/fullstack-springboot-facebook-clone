package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.model.MessageModel;
import com.springboot.fullstack_facebook_clone.dto.request.SendMessageRequest;
import com.springboot.fullstack_facebook_clone.dto.response.MessageResponse;
import com.springboot.fullstack_facebook_clone.entity.Chat;
import com.springboot.fullstack_facebook_clone.entity.Message;

public interface MessageService {

    void sendMessage(String email, SendMessageRequest request);
    MessageResponse fetchAllChatMessages (Long chatId, int pageNo, int pageSize);
    MessageModel getLastMessage(Long chatId);
    public void sendWStoGroupChat(Chat chat, Message message, MessageModel messageModel);
}
