package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.service.PostLikeService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
public class LikeController {

    private final PostLikeService postLikeService;
    private final UserService userService;
    @PutMapping("/{postId}")
    public void likePost(@PathVariable Long postId){
        String currentUser = userService.getAuthenticatedUser();
        postLikeService.likePost(currentUser, postId);
    }
}
