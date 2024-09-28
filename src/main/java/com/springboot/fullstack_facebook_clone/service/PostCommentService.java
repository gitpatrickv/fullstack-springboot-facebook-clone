package com.springboot.fullstack_facebook_clone.service;

import org.springframework.web.multipart.MultipartFile;

public interface PostCommentService {

    void writePostComment(String email, Long postId, String comment, MultipartFile file);
}
