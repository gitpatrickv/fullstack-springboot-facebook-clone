package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.dto.model.MessageModel;
import com.springboot.fullstack_facebook_clone.dto.request.SendMessageRequest;
import com.springboot.fullstack_facebook_clone.dto.response.MessageResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PageResponse;
import com.springboot.fullstack_facebook_clone.entity.Chat;
import com.springboot.fullstack_facebook_clone.entity.Message;
import com.springboot.fullstack_facebook_clone.entity.User;
import com.springboot.fullstack_facebook_clone.entity.constants.ChatType;
import com.springboot.fullstack_facebook_clone.repository.ChatRepository;
import com.springboot.fullstack_facebook_clone.repository.MessageRepository;
import com.springboot.fullstack_facebook_clone.repository.UserRepository;
import com.springboot.fullstack_facebook_clone.service.MessageService;
import com.springboot.fullstack_facebook_clone.utils.Pagination;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import com.springboot.fullstack_facebook_clone.utils.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private final Pagination pagination;
    private final MessageMapper messageMapper;

    @Override
    public void sendMessage(String email, SendMessageRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Optional<Chat> chat = chatRepository.findById(request.getChatId());

        if(chat.isPresent()) {
            Message message = new Message();
            message.setMessage(request.getText());
            message.setTimestamp(LocalDateTime.now());
            message.setChat(chat.get());
            message.setSender(user);
            Message savedMessage = messageRepository.save(message);

            Chat updateChat = chat.get();
            updateChat.setTimestamp(LocalDateTime.now());
            chatRepository.save(updateChat);

            MessageModel messageModel = messageMapper.mapEntityToModel(savedMessage);

            if (chat.get().getChatType().equals(ChatType.PRIVATE_CHAT)) {
                User otherUser = chat.get().getUsers()
                        .stream()
                        .filter(user1 -> !user1.getUserId().equals(user.getUserId()))
                        .findFirst()
                        .orElse(null);

                if (otherUser != null) {
                    log.info("sending WS chat to {} with payload {}", otherUser.getEmail(), messageModel);
                    try {
                        messagingTemplate.convertAndSendToUser(otherUser.getEmail(), "/chat", messageModel);
                    } catch (Exception e) {
                        log.error("Error sending chat: {}", e.getMessage());
                    }
                }
            }

            if (chat.get().getChatType().equals(ChatType.GROUP_CHAT)) {
                log.info("sending WS group chat to {} with payload {}", chat.get().getGroupChatName(), messageModel);
                try {
                    messagingTemplate.convertAndSend("/topic/chat/" + savedMessage.getChat().getChatId(), messageModel);
                } catch (Exception e) {
                    log.error("Error sending chat: {}", e.getMessage());
                }

            }
        }
    }

    @Override
    public MessageResponse fetchAllChatMessages(Long chatId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Message> messages = messageRepository.findChatMessages(chatId, pageable);
        PageResponse pageResponse = pagination.getPagination(messages);

        List<MessageModel> messageModelList = new ArrayList<>();

        for(Message message : messages) {
            MessageModel messageModel = messageMapper.mapEntityToModel(message);
            messageModelList.add(messageModel);
        }

        return new MessageResponse(messageModelList, pageResponse);
    }

    @Override
    public MessageModel getLastMessage(Long chatId) {
        PageRequest pageRequest = PageRequest.of(0, 1);
        List<Message> messages = messageRepository.findLastMessageByChatId(chatId, pageRequest);
        return messages.stream()
                .findFirst()
                .map(messageMapper::mapEntityToModel)
                .orElse(null);
    }


}
