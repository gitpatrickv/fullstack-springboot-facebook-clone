package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.service.FriendshipService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
@Slf4j
public class FriendshipController {

    private final FriendshipService friendshipService;
    private final UserService userService;
    @PostMapping("/add/{strangerUserId}")
    public void addToFriend(@PathVariable("strangerUserId") Long strangerUserId) {
        String currentUser = userService.getAuthenticatedUser();
        friendshipService.addToFriend(currentUser, strangerUserId);
    }
    @PostMapping("/accept/{strangerUserId}")
    public void acceptFriendRequest(@PathVariable("strangerUserId") Long strangerUserId) {
        String currentUser = userService.getAuthenticatedUser();
        friendshipService.acceptFriendRequest(currentUser,strangerUserId);
    }
}

