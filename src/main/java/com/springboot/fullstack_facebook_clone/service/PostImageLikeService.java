package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.response.LikeResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostLikeCountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostLikeUserListResponse;

import java.util.List;

public interface PostImageLikeService {

    void likePostImage(String email, Long postImageId);
    LikeResponse getPostImageLike(String email, Long postImageId);
    PostLikeCountResponse getPostImageLikeCount(Long postImageId);
    List<PostLikeUserListResponse> getPostImageLikeUserList(Long postImageId);
}
