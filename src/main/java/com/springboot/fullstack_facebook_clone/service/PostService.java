package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.request.SharePostRequest;
import com.springboot.fullstack_facebook_clone.dto.response.PostListResponse;
import com.springboot.fullstack_facebook_clone.dto.response.SharedPostCountResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {

    void createPost(String email, String content, MultipartFile[] files);
    PostListResponse fetchAllUserPosts(Long userId, int pageNo, int pageSize);
    void sharePost(String email, Long postId, SharePostRequest request);
    SharedPostCountResponse getSharedPostCount(Long postId);

}
