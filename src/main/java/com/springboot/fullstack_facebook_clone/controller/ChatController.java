package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.dto.model.ChatModel;
import com.springboot.fullstack_facebook_clone.dto.request.AddUserToGroupChatRequest;
import com.springboot.fullstack_facebook_clone.dto.request.GroupChatNameRequest;
import com.springboot.fullstack_facebook_clone.dto.request.GroupChatRequest;
import com.springboot.fullstack_facebook_clone.dto.response.ChatIdResponse;
import com.springboot.fullstack_facebook_clone.dto.response.ChatResponse;
import com.springboot.fullstack_facebook_clone.entity.constants.LeaveReason;
import com.springboot.fullstack_facebook_clone.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ChatIdResponse createGroupChat(@PathVariable("userId") Long userId, @RequestBody @Valid GroupChatRequest request) {
        return chatService.createGroupChat(userId,request);
    }
    @PostMapping("/group/upload/image/{chatId}")
    public void uploadGroupChatPhoto(@PathVariable("chatId") Long chatId,
                                     @RequestParam(value = "file") MultipartFile files){
        chatService.uploadGroupChatPhoto(chatId,files);
    }
    @PostMapping("/group/change/name")
    public void updateGroupChatName(@RequestBody @Valid GroupChatNameRequest request) {
        chatService.updateGroupChatName(request);
    }

    @PostMapping("/group/add/user/{chatId}")
    public void addUserToGroupChat(@PathVariable("chatId") Long chatId,
                                   @RequestBody @Valid AddUserToGroupChatRequest request){
        chatService.addUserToGroupChat(chatId, request);
    }
    @PostMapping("/group/leave/{chatId}/{userId}/{leaveReason}")
    public void leaveGroupChat(@PathVariable("chatId") Long chatId,
                               @PathVariable("userId") Long userId,
                               @PathVariable("leaveReason") LeaveReason leaveReason) {
        chatService.leaveGroupChat(chatId,userId, leaveReason);
    }
}
