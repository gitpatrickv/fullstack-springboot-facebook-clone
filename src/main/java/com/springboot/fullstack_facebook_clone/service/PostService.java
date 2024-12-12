package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.model.PostModel;
import com.springboot.fullstack_facebook_clone.dto.request.SharePostRequest;
import com.springboot.fullstack_facebook_clone.dto.response.PostListResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostResponse;
import com.springboot.fullstack_facebook_clone.dto.response.SharedPostCountResponse;
import com.springboot.fullstack_facebook_clone.entity.Post;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {

    void createPost(Long userId, String content, MultipartFile[] files);
    PostListResponse fetchAllUserPosts(Long userId, int pageNo, int pageSize);
    PostListResponse fetchAllPosts(int pageNo, int pageSize);
    void sharePost(Long postId, SharePostRequest request);
    void sharePostImage(Long postImageId, Long postId, SharePostRequest request);
    SharedPostCountResponse getSharedPostImageCount(Long postImageId);
    SharedPostCountResponse getSharedPostCount(Long postId);
    void deletePost(Long postId);
    PostResponse findPostCreatorById(Long postId);
    PostModel getPostById(Long postId);
    Post getPost(Long postId);
}
