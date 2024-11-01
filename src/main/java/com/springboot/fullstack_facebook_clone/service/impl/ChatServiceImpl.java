package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.dto.model.ChatModel;
import com.springboot.fullstack_facebook_clone.dto.model.UserDataModel;
import com.springboot.fullstack_facebook_clone.dto.response.ChatIdResponse;
import com.springboot.fullstack_facebook_clone.dto.response.ChatResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PageResponse;
import com.springboot.fullstack_facebook_clone.entity.Chat;
import com.springboot.fullstack_facebook_clone.entity.User;
import com.springboot.fullstack_facebook_clone.entity.constants.ChatType;
import com.springboot.fullstack_facebook_clone.repository.ChatRepository;
import com.springboot.fullstack_facebook_clone.repository.UserRepository;
import com.springboot.fullstack_facebook_clone.service.ChatService;
import com.springboot.fullstack_facebook_clone.utils.Pagination;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import com.springboot.fullstack_facebook_clone.utils.mapper.ChatMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Chat> chats = chatRepository.findAllUserChats(userId, pageable);
        PageResponse pageResponse = pagination.getPagination(chats);

        List<ChatModel> chatModels = new ArrayList<>();

        for(Chat chat : chats){

            if(chat.getChatType().equals(ChatType.GROUP_CHAT)) {
                ChatModel chatModel = chatMapper.mapEntityToModel(chat);
                chatModels.add(chatModel);
            }

            if(chat.getChatType().equals(ChatType.PRIVATE_CHAT)) {
                ChatModel chatModel = new ChatModel();
                chatModel.setChatId(chat.getChatId());
                chatModel.setChatType(chat.getChatType());

                User otherUser = chat.getUsers()
                        .stream()
                        .filter(user1 -> !user1.getUserId().equals(userId))
                        .findFirst()
                        .orElse(null);

                if(otherUser != null) {
                    UserDataModel userDataModel = new UserDataModel();
                    userDataModel.setUserId(otherUser.getUserId());
                    userDataModel.setFirstName(otherUser.getFirstName());
                    userDataModel.setLastName(otherUser.getLastName());
                    userDataModel.setProfilePicture(otherUser.getProfilePicture());

                    chatModel.setPrivateChatUser(userDataModel);
                    chatModels.add(chatModel);
                }
            }
        }


        return new ChatResponse(chatModels,pageResponse);
    }
}


