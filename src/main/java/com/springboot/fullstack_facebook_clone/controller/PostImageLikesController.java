package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.dto.response.LikeResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostLikeCountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostLikeUserListResponse;
import com.springboot.fullstack_facebook_clone.service.PostImageLikeService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostImageLikesController {

    private final PostImageLikeService postImageLikeService;
    private final UserService userService;
    @PutMapping("/{postImageId}/image/like")
    public void likePostImage(@PathVariable("postImageId") Long postImageId) {
        String currentUser = userService.getAuthenticatedUser();
        postImageLikeService.likePostImage(currentUser, postImageId);
    }

    @GetMapping("/{postImageId}/image/like")
    public LikeResponse getPostLike(@PathVariable("postImageId") Long postImageId){
        String currentUser = userService.getAuthenticatedUser();
        return postImageLikeService.getPostImageLike(currentUser, postImageId);
    }
    @GetMapping("/{postImageId}/image/like/count")
    public PostLikeCountResponse getPostImageLikeCount(@PathVariable("postImageId") Long postImageId) {
        return postImageLikeService.getPostImageLikeCount(postImageId);
    }

    @GetMapping("/{postImageId}/image/like/user/list")
    public List<PostLikeUserListResponse> getPostImageLikeUserList(@PathVariable("postImageId") Long postImageId){
        return postImageLikeService.getPostImageLikeUserList(postImageId);
    }

}
