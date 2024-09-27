package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.response.LikeResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostLikeCountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostLikeUserListResponse;

import java.util.List;

public interface PostLikeService {

    void likePost(String email, Long postId);
    LikeResponse getPostLike(String email, Long postId);
    PostLikeCountResponse getPostLikeCount(Long postId);
    List<PostLikeUserListResponse> getPostLikeUserList(Long postId);
}
