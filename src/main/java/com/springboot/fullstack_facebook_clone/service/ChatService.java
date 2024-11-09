package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.model.ChatModel;
import com.springboot.fullstack_facebook_clone.dto.request.GroupChatRequest;
import com.springboot.fullstack_facebook_clone.dto.response.ChatIdResponse;
import com.springboot.fullstack_facebook_clone.dto.response.ChatResponse;

public interface ChatService {

    ChatIdResponse chatUser(Long userId, Long friendId);
    ChatResponse fetchAllUserChats(Long userId, int pageNo, int pageSize);
    ChatModel findChatById(Long chatId, Long userId);
    void createGroupChat(Long userId, GroupChatRequest request);
}
