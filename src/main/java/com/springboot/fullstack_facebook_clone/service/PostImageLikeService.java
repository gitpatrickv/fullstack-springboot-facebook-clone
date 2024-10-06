package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.response.LikeResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostLikeCountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.UserListResponse;

public interface PostImageLikeService {

    void likePostImage(String email, Long postImageId);
    LikeResponse getPostImageLike(String email, Long postImageId);
    PostLikeCountResponse getPostImageLikeCount(Long postImageId);
    UserListResponse getPostImageLikeUserList(Long postImageId, int pageNo, int pageSize);
}
