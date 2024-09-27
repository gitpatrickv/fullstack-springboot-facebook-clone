package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.dto.response.LikeResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostLikeCountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostLikeUserListResponse;
import com.springboot.fullstack_facebook_clone.service.PostLikeService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{postId}/like/count")
    public PostLikeCountResponse getPostLikeCount(@PathVariable Long postId){
        return postLikeService.getPostLikeCount(postId);
    }
    @GetMapping("/{postId}/like/user/list")
    public List<PostLikeUserListResponse> getPostLikeUserList(@PathVariable Long postId){
        return postLikeService.getPostLikeUserList(postId);
    }
}
