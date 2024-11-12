package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.dto.model.ChatModel;
import com.springboot.fullstack_facebook_clone.dto.model.UserDataModel;
import com.springboot.fullstack_facebook_clone.dto.request.GroupChatRequest;
import com.springboot.fullstack_facebook_clone.dto.response.ChatIdResponse;
import com.springboot.fullstack_facebook_clone.dto.response.ChatResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PageResponse;
import com.springboot.fullstack_facebook_clone.entity.Chat;
import com.springboot.fullstack_facebook_clone.entity.Message;
import com.springboot.fullstack_facebook_clone.entity.User;
import com.springboot.fullstack_facebook_clone.entity.constants.ChatType;
import com.springboot.fullstack_facebook_clone.repository.ChatRepository;
import com.springboot.fullstack_facebook_clone.repository.MessageRepository;
import com.springboot.fullstack_facebook_clone.repository.UserRepository;
import com.springboot.fullstack_facebook_clone.service.ChatService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import com.springboot.fullstack_facebook_clone.utils.Pagination;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import com.springboot.fullstack_facebook_clone.utils.mapper.ChatMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatMapper chatMapper;
    private final Pagination pagination;
    private final MessageRepository messageRepository;
    private final UserService userService;

    @Override
    public ChatIdResponse chatUser(Long userId, Long friendId) {
        Optional<Chat> optionalChat = chatRepository.findExistingChat(userId,friendId,ChatType.PRIVATE_CHAT);
        Chat chat;

        if(optionalChat.isPresent()){
            chat = optionalChat.get();
        } else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + userId));
            User friend = userRepository.findById(friendId)
                    .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + friendId));

            Set<User> users = new HashSet<>(Arrays.asList(user,friend));

            chat = new Chat();
            chat.setChatType(ChatType.PRIVATE_CHAT);
            chat.setTimestamp(LocalDateTime.now());
            chat.setUsers(users);
            Chat savedChat = chatRepository.save(chat);

            user.getChats().add(savedChat);
            friend.getChats().add(savedChat);
        }

        ChatIdResponse chatIdResponse = new ChatIdResponse();
        chatIdResponse.setChatId(chat.getChatId());
        return chatIdResponse;
    }

    @Override
    public ChatResponse fetchAllUserChats(Long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, StringUtil.TIMESTAMP));
        Page<Chat> chats = chatRepository.findAllUserChats(userId, pageable);
        PageResponse pageResponse = pagination.getPagination(chats);

        List<ChatModel> chatModels = chats.stream()
                .map(chat -> this.mapChatToModel(chat,userId))
                .toList();

        return new ChatResponse(chatModels,pageResponse);
    }

    @Override
    public ChatModel findChatById(Long chatId, Long userId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.CHAT_NOT_FOUND + chatId));
        return this.mapChatToModel(chat, userId);
    }

    @Override
    public ChatIdResponse createGroupChat(Long userId, GroupChatRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + userId));

        request.getFriendId().add(user.getUserId());

        List<User> users =  userRepository.findAllById(request.getFriendId());
        Set<User> userSet = new HashSet<>(users);

        Chat chat = new Chat();
        chat.setChatType(ChatType.GROUP_CHAT);
        chat.setTimestamp(LocalDateTime.now());
        chat.setUsers(userSet);
        Chat savedChat = chatRepository.save(chat);

        for(User user1 : userSet){
            user1.getChats().add(savedChat);
        }

        if(savedChat.getChatId() != null) {
            Message message = new Message();
            message.setMessage(request.getText());
            message.setTimestamp(LocalDateTime.now());
            message.setChat(savedChat);
            message.setSender(user);
            messageRepository.save(message);
        }

        ChatIdResponse chatIdResponse = new ChatIdResponse();
        chatIdResponse.setChatId(savedChat.getChatId());

        return chatIdResponse;
    }

    @Override
    public void uploadGroupChatPhoto(Long chatId, MultipartFile file) {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);

        if(optionalChat.isPresent()
                && optionalChat.get().getChatType().equals(ChatType.GROUP_CHAT)){
            if(file != null) {
                Chat chat = optionalChat.get();
                chat.setGroupChatImage(userService.processImage(file));
                chatRepository.save(chat);
            }
        }
    }

    private ChatModel mapChatToModel(Chat chat, Long userId) {
        ChatType chatType = chat.getChatType();

        if (chatType.equals(ChatType.GROUP_CHAT)) {
            return chatMapper.mapEntityToModel(chat);
        }

        if (chatType.equals(ChatType.PRIVATE_CHAT)) {
            ChatModel chatModel = new ChatModel();
            chatModel.setChatId(chat.getChatId());
            chatModel.setChatType(chatType);

            User otherUser = chat.getUsers()
                    .stream()
                    .filter(user -> !user.getUserId().equals(userId))
                    .findFirst()
                    .orElse(null);

            if (otherUser != null) {
                UserDataModel userDataModel = new UserDataModel();
                userDataModel.setUserId(otherUser.getUserId());
                userDataModel.setFirstName(otherUser.getFirstName());
                userDataModel.setLastName(otherUser.getLastName());
                userDataModel.setProfilePicture(otherUser.getProfilePicture());

                chatModel.setPrivateChatUser(userDataModel);
            }
            return chatModel;
        }
        return null;
    }
}
