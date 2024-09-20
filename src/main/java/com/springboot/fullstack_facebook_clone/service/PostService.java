package com.springboot.fullstack_facebook_clone.service;

import org.springframework.web.multipart.MultipartFile;

public interface PostService {

    void createPost(String email, String content, MultipartFile[] files);
}
