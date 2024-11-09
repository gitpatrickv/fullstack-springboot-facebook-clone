package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.dto.model.ChatModel;
import com.springboot.fullstack_facebook_clone.dto.request.GroupChatRequest;
import com.springboot.fullstack_facebook_clone.dto.response.ChatIdResponse;
import com.springboot.fullstack_facebook_clone.dto.response.ChatResponse;
import com.springboot.fullstack_facebook_clone.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    @PostMapping("/{userId}/{friendId}")
    public ChatIdResponse chatUser(@PathVariable("userId") Long userId,
                                     @PathVariable("friendId") Long friendId){
       return chatService.chatUser(userId,friendId);
    }

    @GetMapping("/{userId}")
    public ChatResponse fetchAllUserChats(@PathVariable("userId") Long userId,
                                         @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return chatService.fetchAllUserChats(userId,pageNo,pageSize);
    }
    @GetMapping("/get/{chatId}/{userId}")
    public ChatModel findChatById(@PathVariable("chatId") Long chatId,
                                  @PathVariable("userId") Long userId) {
        return chatService.findChatById(chatId,userId);
    }
    @PostMapping("/group/create/{userId}")
    public void createGroupChat(@PathVariable("userId") Long userId, @RequestBody @Valid GroupChatRequest request) {
        chatService.createGroupChat(userId,request);
    }
}
