package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.dto.model.NotificationModel;
import com.springboot.fullstack_facebook_clone.dto.model.UserDataModel;
import com.springboot.fullstack_facebook_clone.dto.response.CountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.FriendshipStatusResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PageResponse;
import com.springboot.fullstack_facebook_clone.dto.response.UserListResponse;
import com.springboot.fullstack_facebook_clone.entity.Friendship;
import com.springboot.fullstack_facebook_clone.entity.Notification;
import com.springboot.fullstack_facebook_clone.entity.User;
import com.springboot.fullstack_facebook_clone.entity.constants.FriendshipStatus;
import com.springboot.fullstack_facebook_clone.entity.constants.NotificationType;
import com.springboot.fullstack_facebook_clone.repository.FriendshipRepository;
import com.springboot.fullstack_facebook_clone.repository.NotificationRepository;
import com.springboot.fullstack_facebook_clone.repository.UserRepository;
import com.springboot.fullstack_facebook_clone.service.FriendshipService;
import com.springboot.fullstack_facebook_clone.service.NotificationService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import com.springboot.fullstack_facebook_clone.utils.Pagination;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import com.springboot.fullstack_facebook_clone.utils.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendshipServiceImpl implements FriendshipService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final UserService userService;
    private final Pagination pagination;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final NotificationService notificationService;

    @Override
    public void addToFriend(String currentUser, Long strangerUserId) {
        User user = userRepository.findByEmail(currentUser)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + currentUser));
        Optional<Friendship> friendRequest = friendshipRepository.findByFriendship(user.getUserId(), strangerUserId, FriendshipStatus.PENDING);
        Optional<Friendship> friendRequestFromOtherUser = friendshipRepository.findByFriendship(strangerUserId, user.getUserId(), FriendshipStatus.PENDING);
        if(user.getUserId().equals(strangerUserId)){
            throw new IllegalArgumentException(StringUtil.FRIEND_REQUEST_NOT_ALLOWED);
        }

        if(friendRequestFromOtherUser.isPresent()){
            throw new IllegalArgumentException(StringUtil.FRIEND_REQUEST_ALREADY_EXISTS);
        }

        if(friendRequest.isPresent()){
            friendshipRepository.deleteByUser_UserIdAndFriends_UserId(user.getUserId(), strangerUserId);
            notificationRepository.deleteByNotificationTypeAndSender_UserIdAndReceiver_UserId(NotificationType.FRIEND_REQUEST, user.getUserId(), strangerUserId);
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

            Notification notification = new Notification();
            notification.setMessage("sent you a friend request.");
            notification.setRead(false);
            notification.setNotificationType(NotificationType.FRIEND_REQUEST);
            notification.setTimestamp(LocalDateTime.now());
            notification.setReceiver(stranger);
            notification.setSender(user);

            Notification savedNotification = notificationRepository.save(notification);

            NotificationModel notificationModel = notificationMapper.mapEntityToModel(savedNotification);
            notificationModel.getSender().setUniqueId(user.getUserId() + 1000);

            notificationService.sendNotification(stranger.getEmail(), notificationModel);
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

            Notification notification = new Notification();
            notification.setMessage("accepted your friend request.");
            notification.setRead(false);
            notification.setNotificationType(NotificationType.FRIEND_ACCEPTED);
            notification.setTimestamp(LocalDateTime.now());
            notification.setReceiver(stranger);
            notification.setSender(user);

            Notification savedNotification = notificationRepository.save(notification);

            NotificationModel notificationModel = notificationMapper.mapEntityToModel(savedNotification);
            notificationModel.getSender().setUniqueId(user.getUserId() + 1000);

            notificationService.sendNotification(stranger.getEmail(), notificationModel);

            Notification getNotification = notificationRepository.findByNotificationTypeAndSender_UserIdAndReceiver_UserId(NotificationType.FRIEND_REQUEST, strangerUserId, user.getUserId());
            getNotification.setNotificationType(NotificationType.FRIEND_ACCEPTED);
            notificationRepository.save(getNotification);
        } else {
            throw new NoSuchElementException(StringUtil.FRIEND_REQUEST_NOT_FOUND);
        }
    }

    @Override
    public UserListResponse fetchAllFriendRequest(Long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, StringUtil.TIMESTAMP));
        Page<Friendship> friendships = friendshipRepository.findAllByStatusAndFriends_UserId(FriendshipStatus.PENDING, userId, pageable);
        PageResponse pageResponse = pagination.getPagination(friendships);

        List<UserDataModel> userDataModels = this.getUserDataModels(friendships);
        return new UserListResponse(userDataModels,pageResponse);
    }

    @Override
    public UserListResponse fetchAllUserFriends(Long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, StringUtil.TIMESTAMP));
        Page<Friendship> friendships = friendshipRepository.findAllByStatusAndUser_UserId(FriendshipStatus.FRIENDS, userId, pageable);
        PageResponse pageResponse = pagination.getPagination(friendships);

        List<UserDataModel> userDataModels = this.getFriendsDataModels(friendships);
        return new UserListResponse(userDataModels,pageResponse);
    }

    @Override
    public UserListResponse fetchAllFriendSuggestions(Long userid, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<User> userList = friendshipRepository.findFriendSuggestions(userid,pageable);
        PageResponse pageResponse = userService.getUserPagination(userList);
        List<UserDataModel> userDataModels = userService.getUserDataModels(userList);

        return new UserListResponse(userDataModels,pageResponse);
    }

    @Override
    public FriendshipStatusResponse getFriendshipStatus(String currentUser, Long friendId, boolean isRequestStatus) {
        User user = userRepository.findByEmail(currentUser)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + currentUser));
        Optional<Friendship> friendship;

        if(isRequestStatus){
            friendship = friendshipRepository.findByUser_UserIdAndFriends_UserId(user.getUserId(), friendId);
        } else {
            friendship = friendshipRepository.findByFriendship(friendId, user.getUserId(), FriendshipStatus.PENDING);
        }

        if(friendship.isEmpty()){
            return null;
        }

        FriendshipStatusResponse friendshipStatusResponse = new FriendshipStatusResponse();
        friendshipStatusResponse.setStatus(friendship.get().getStatus().toString());

        return friendshipStatusResponse;
    }

    @Override
    public void unfriend(Long userId, Long friendId) {

        Optional<Friendship> friendship1 = friendshipRepository.findByFriendship(userId, friendId, FriendshipStatus.FRIENDS);
        friendship1.ifPresent(friendshipRepository::delete);

        Optional<Friendship> friendship2 = friendshipRepository.findByFriendship(friendId, userId, FriendshipStatus.FRIENDS);
        friendship2.ifPresent(friendshipRepository::delete);
    }

    @Override
    public void deleteFriendRequest(Long userId, Long strangerId) {
        Optional<Friendship> friendship = friendshipRepository.findByFriendship(strangerId, userId, FriendshipStatus.PENDING);
        friendship.ifPresent(friendshipRepository::delete);
    }

    @Override
    public CountResponse getFriendListCount(Long userId) {
        Long count = friendshipRepository.getFriendListCount(userId);

        CountResponse countResponse = new CountResponse();
        countResponse.setCount(count);
        return countResponse;
    }

    private List<UserDataModel> getFriendsDataModels(Page<Friendship> friendships) {
        List<UserDataModel> userDataModels = new ArrayList<>();

        for(Friendship friendship : friendships){
            UserDataModel userDataModel = new UserDataModel();
            userDataModel.setUniqueId(friendship.getFriendshipId());
            userDataModel.setUserId(friendship.getFriends().getUserId());
            userDataModel.setFirstName(friendship.getFriends().getFirstName());
            userDataModel.setLastName(friendship.getFriends().getLastName());
            userDataModel.setProfilePicture(friendship.getFriends().getProfilePicture());
            userDataModels.add(userDataModel);
        }

        return userDataModels;
    }

    private List<UserDataModel> getUserDataModels(Page<Friendship> friendships) {
        List<UserDataModel> userDataModels = new ArrayList<>();

        for(Friendship friendship : friendships){
            UserDataModel userDataModel = new UserDataModel();
            userDataModel.setUniqueId(friendship.getFriendshipId());
            userDataModel.setUserId(friendship.getUser().getUserId());
            userDataModel.setFirstName(friendship.getUser().getFirstName());
            userDataModel.setLastName(friendship.getUser().getLastName());
            userDataModel.setProfilePicture(friendship.getUser().getProfilePicture());
            userDataModels.add(userDataModel);
        }
        return userDataModels;
    }
}
