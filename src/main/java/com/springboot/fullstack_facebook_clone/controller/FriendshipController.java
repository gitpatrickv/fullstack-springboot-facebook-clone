package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.dto.response.CountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.FriendshipStatusResponse;
import com.springboot.fullstack_facebook_clone.dto.response.UserListResponse;
import com.springboot.fullstack_facebook_clone.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
@Slf4j
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping("/add/{strangerUserId}")
    public void addToFriend(@PathVariable("strangerUserId") Long strangerUserId) {
        friendshipService.addToFriend(strangerUserId);
    }
    @PostMapping("/accept/{strangerUserId}")
    public void acceptFriendRequest(@PathVariable("strangerUserId") Long strangerUserId) {
        friendshipService.acceptFriendRequest(strangerUserId);
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
        return friendshipService.getFriendshipStatus(friendId, true);
    }

    @GetMapping("/status/request/{friendId}")
    public FriendshipStatusResponse getFriendRequestStatus(@PathVariable("friendId") Long friendId) {
        return friendshipService.getFriendshipStatus(friendId, false);
    }
    @DeleteMapping("/unfriend/{userId}/{friendId}")
    public void unfriend(@PathVariable("userId") Long userId, @PathVariable("friendId") Long friendId) {
        friendshipService.unfriend(userId,friendId);
    }
    @DeleteMapping("/delete/{userId}/{strangerId}")
    public void deleteFriendRequest(@PathVariable("userId") Long userId, @PathVariable("strangerId") Long strangerId){
        friendshipService.deleteFriendRequest(userId,strangerId);
    }
    @GetMapping("/count/{userId}")
    public CountResponse getFriendListCount(@PathVariable("userId") Long userId) {
        return friendshipService.getFriendListCount(userId);
    }
    @GetMapping("/suggestions/{userId}")
    public UserListResponse fetchAllFriendSuggestions(@PathVariable("userId") Long userid,
                                                      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return friendshipService.fetchAllFriendSuggestions(userid,pageNo,pageSize);
    }
}

