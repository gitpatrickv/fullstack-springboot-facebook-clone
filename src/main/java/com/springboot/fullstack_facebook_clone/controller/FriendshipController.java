package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.dto.response.CountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.FriendshipStatusResponse;
import com.springboot.fullstack_facebook_clone.dto.response.UserListResponse;
import com.springboot.fullstack_facebook_clone.service.FriendshipService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/request/list/{userId}")
    public UserListResponse fetchAllFriendRequest(@PathVariable("userId") Long userId,
                                                  @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                  @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        return friendshipService.fetchAllFriendRequest(userId ,pageNo,pageSize);
    }
    @GetMapping("/list/{userId}")
    public UserListResponse fetchAllUserFriends(@PathVariable("userId") Long userId,
                                                @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        return friendshipService.fetchAllUserFriends(userId,pageNo,pageSize);
    }
    @GetMapping("/status/{friendId}")
    public FriendshipStatusResponse getFriendshipStatus(@PathVariable("friendId") Long friendId) {
        String currentUser = userService.getAuthenticatedUser();
        return friendshipService.getFriendshipStatus(currentUser,friendId, true);
    }

    @GetMapping("/status/request/{friendId}")
    public FriendshipStatusResponse getFriendRequestStatus(@PathVariable("friendId") Long friendId) {
        String currentUser = userService.getAuthenticatedUser();
        return friendshipService.getFriendshipStatus(currentUser,friendId, false);
    }
    @DeleteMapping("/unfriend/{friendId}")
    public void unfriend(@PathVariable("friendId") Long friendId) {
        String currentUser = userService.getAuthenticatedUser();
        friendshipService.unfriend(currentUser,friendId);
    }
    @DeleteMapping("/delete/{strangerId}")
    public void deleteFriendRequest(@PathVariable("strangerId") Long strangerId){
        String currentUser = userService.getAuthenticatedUser();
        friendshipService.deleteFriendRequest(currentUser,strangerId);
    }
    @GetMapping("/count/{userId}")
    public CountResponse getFriendListCount(@PathVariable("userId") Long userId) {
        return friendshipService.getFriendListCount(userId);
    }
}

