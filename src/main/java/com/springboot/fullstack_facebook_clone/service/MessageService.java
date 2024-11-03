package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.request.SendMessageRequest;

public interface MessageService {

    void sendMessage(SendMessageRequest request);
}
