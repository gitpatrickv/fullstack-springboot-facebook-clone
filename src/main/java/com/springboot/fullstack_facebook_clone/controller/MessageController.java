package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.dto.request.SendMessageRequest;
import com.springboot.fullstack_facebook_clone.dto.response.MessageResponse;
import com.springboot.fullstack_facebook_clone.service.MessageService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    @PostMapping
    public void sendMessage(@RequestBody @Valid SendMessageRequest request) {
        String currentUser = userService.getAuthenticatedUser();
       messageService.sendMessage(currentUser, request);
    }
    @GetMapping("/{chatId}")
    public MessageResponse fetchAllChatMessages(@PathVariable("chatId") Long chatId,
                                                @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return messageService.fetchAllChatMessages(chatId, pageNo, pageSize);
    }
}
