package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.model.PostModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    void createPost(String email, String content, MultipartFile[] files);
    List<PostModel> fetchAllUserPosts(String email);

}
