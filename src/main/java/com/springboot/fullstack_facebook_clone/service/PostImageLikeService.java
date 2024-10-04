package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.response.LikeResponse;

public interface PostImageLikeService {

    void likePostImage(String email, Long postImageId);
    LikeResponse getPostImageLike(String email, Long postImageId);
}
