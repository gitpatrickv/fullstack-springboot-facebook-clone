package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.model.PostModel;
import com.springboot.fullstack_facebook_clone.dto.request.SharePostRequest;
import com.springboot.fullstack_facebook_clone.dto.response.PostListResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostResponse;
import com.springboot.fullstack_facebook_clone.dto.response.SharedPostCountResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {

    void createPost(String email, Long userId, String content, MultipartFile[] files);
    PostListResponse fetchAllUserPosts(Long userId, int pageNo, int pageSize);
    PostListResponse fetchAllPosts(String email, int pageNo, int pageSize);
    void sharePost(String email, Long postId, SharePostRequest request);
    void sharePostImage(String email, Long postImageId, Long postId, SharePostRequest request);
    SharedPostCountResponse getSharedPostImageCount(Long postImageId);
    SharedPostCountResponse getSharedPostCount(Long postId);
    void deletePost(String email, Long postId);
    PostResponse findPostCreatorById(Long postId);
    PostModel getPostById(Long postId);
}
