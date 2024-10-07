package com.springboot.fullstack_facebook_clone.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PostImageService {

    void uploadPostImages(Long postId, MultipartFile[] files);
    byte[] getImages(String filename) throws IOException;
    String processPostImages(Long postId, MultipartFile image);
}
