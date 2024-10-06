package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.response.LikeResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostLikeCountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.UserListResponse;

public interface PostLikeService {

    void likePost(String email, Long postId);
    LikeResponse getPostLike(String email, Long postId);
    PostLikeCountResponse getPostLikeCount(Long postId);
    UserListResponse getPostLikeUserList(Long postId, int pageNo, int pageSize);
}
