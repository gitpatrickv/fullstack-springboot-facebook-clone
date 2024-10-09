package com.springboot.fullstack_facebook_clone.service;

public interface FriendshipService {

    void addToFriend(String currentUser, Long strangerUserId);
    void acceptFriendRequest(String currentUser, Long strangerUserId);
}
