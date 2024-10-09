package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.entity.Friendship;
import com.springboot.fullstack_facebook_clone.entity.User;
import com.springboot.fullstack_facebook_clone.entity.constants.FriendshipStatus;
import com.springboot.fullstack_facebook_clone.repository.FriendshipRepository;
import com.springboot.fullstack_facebook_clone.repository.UserRepository;
import com.springboot.fullstack_facebook_clone.service.FriendshipService;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendshipServiceImpl implements FriendshipService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    @Override
    public void addToFriend(String currentUser, Long strangerUserId) {
        User user = userRepository.findByEmail(currentUser)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + currentUser));
        Optional<Friendship> friendRequest = friendshipRepository.findByFriendship(user.getUserId(), strangerUserId, FriendshipStatus.PENDING);

        if(friendRequest.isPresent()){
            friendshipRepository.deleteByUser_UserIdAndFriends_UserId(user.getUserId(), strangerUserId);
        }
        else {
            User stranger = userRepository.findById(strangerUserId)
                    .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + strangerUserId));

            Friendship friendship = new Friendship();
            friendship.setUser(user);
            friendship.setFriends(stranger);
            friendship.setStatus(FriendshipStatus.PENDING);
            friendship.setTimestamp(LocalDateTime.now());
            friendshipRepository.save(friendship);
        }
    }

    @Override
    public void acceptFriendRequest(String currentUser, Long strangerUserId) {
        User user = userRepository.findByEmail(currentUser)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + currentUser));
        User stranger = userRepository.findById(strangerUserId)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + strangerUserId));

        Optional<Friendship> friendRequest = friendshipRepository.findByFriendship(strangerUserId, user.getUserId(), FriendshipStatus.PENDING);

        if(friendRequest.isPresent()) {

            friendRequest.get().setStatus(FriendshipStatus.FRIENDS);

            Friendship friendship = new Friendship();
            friendship.setUser(user);
            friendship.setFriends(stranger);
            friendship.setStatus(FriendshipStatus.FRIENDS);
            friendship.setTimestamp(LocalDateTime.now());
            friendshipRepository.save(friendship);
        }
    }
}
