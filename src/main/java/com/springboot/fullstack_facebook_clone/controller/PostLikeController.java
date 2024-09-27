package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.dto.response.LikeResponse;
import com.springboot.fullstack_facebook_clone.service.PostLikeService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;
    private final UserService userService;
    @PutMapping("/{postId}/like")
    public void likePost(@PathVariable Long postId){
        String currentUser = userService.getAuthenticatedUser();
        postLikeService.likePost(currentUser, postId);
    }

    @GetMapping("/{postId}/like")
    public LikeResponse getPostLike(@PathVariable Long postId){
        String currentUser = userService.getAuthenticatedUser();
        return postLikeService.getPostLike(currentUser, postId);
    }
}
