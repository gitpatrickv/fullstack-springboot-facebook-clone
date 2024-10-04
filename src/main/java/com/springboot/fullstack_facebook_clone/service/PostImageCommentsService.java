package com.springboot.fullstack_facebook_clone.service;

import org.springframework.web.multipart.MultipartFile;

public interface PostImageCommentsService {

    void writePostImageComment(String email, Long postImageId, String comment, MultipartFile file);
}
