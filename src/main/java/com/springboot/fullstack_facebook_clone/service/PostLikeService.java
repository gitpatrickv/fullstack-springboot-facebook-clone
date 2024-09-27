package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.response.LikeResponse;

public interface PostLikeService {

    void likePost(String email, Long postId);
    LikeResponse getPostLike(String email, Long postId);
}
