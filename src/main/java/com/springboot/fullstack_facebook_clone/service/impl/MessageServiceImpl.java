package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.dto.request.SendMessageRequest;
import com.springboot.fullstack_facebook_clone.entity.Chat;
import com.springboot.fullstack_facebook_clone.entity.Message;
import com.springboot.fullstack_facebook_clone.entity.User;
import com.springboot.fullstack_facebook_clone.entity.constants.ChatType;
import com.springboot.fullstack_facebook_clone.repository.ChatRepository;
import com.springboot.fullstack_facebook_clone.repository.MessageRepository;
import com.springboot.fullstack_facebook_clone.repository.UserRepository;
import com.springboot.fullstack_facebook_clone.service.MessageService;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendMessage(SendMessageRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + request.getUserId()));
        Optional<Chat> chat = chatRepository.findById(request.getChatId());

        if(chat.isPresent()) {
            Message message = new Message();
            message.setMessage(request.getText());
            message.setTimestamp(LocalDateTime.now());
            message.setChat(chat.get());
            message.setSender(user);
            messageRepository.save(message);

            if (chat.get().getChatType().equals(ChatType.PRIVATE_CHAT)) {
                User otherUser = chat.get().getUsers()
                        .stream()
                        .filter(user1 -> !user1.getUserId().equals(user.getUserId()))
                        .findFirst()
                        .orElse(null);

                if (otherUser != null) {
                    log.info("sending WS chat to {} with payload {}", chat.get().getChatId(), request);
                    try {
                        messagingTemplate.convertAndSendToUser(otherUser.getEmail(), "/chat", request);
                    } catch (Exception e) {
                        log.error("Error sending chat: {}", e.getMessage());
                    }
                }
            }

        }
    }
}
