package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.model.ChatModel;
import com.springboot.fullstack_facebook_clone.dto.request.AddUserToGroupChatRequest;
import com.springboot.fullstack_facebook_clone.dto.request.GroupChatNameRequest;
import com.springboot.fullstack_facebook_clone.dto.request.GroupChatRequest;
import com.springboot.fullstack_facebook_clone.dto.response.ChatIdResponse;
import com.springboot.fullstack_facebook_clone.dto.response.ChatResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ChatService {

    ChatIdResponse chatUser(Long userId, Long friendId);
    ChatResponse fetchAllUserChats(Long userId, int pageNo, int pageSize);
    ChatModel findChatById(Long chatId, Long userId);
    ChatIdResponse createGroupChat(Long userId, GroupChatRequest request);
    void uploadGroupChatPhoto(Long chatId, MultipartFile file);
    void updateGroupChatName(GroupChatNameRequest request);
    void addUserToGroupChat(Long chatId, AddUserToGroupChatRequest request);
}
