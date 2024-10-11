package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.response.CountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.FriendshipStatusResponse;
import com.springboot.fullstack_facebook_clone.dto.response.UserListResponse;

public interface FriendshipService {

    void addToFriend(String currentUser, Long strangerUserId);
    void acceptFriendRequest(String currentUser, Long strangerUserId);
    UserListResponse fetchAllFriendRequest(Long userId, int pageNo, int pageSize);
    UserListResponse fetchAllUserFriends(Long userId,  int pageNo, int pageSize);
    FriendshipStatusResponse getFriendshipStatus(String currentUser, Long friendId, boolean isRequestStatus);
    void unfriend(Long userId, Long friendId);
    void deleteFriendRequest(Long userId, Long strangerId);
    CountResponse getFriendListCount(Long userId);
}
