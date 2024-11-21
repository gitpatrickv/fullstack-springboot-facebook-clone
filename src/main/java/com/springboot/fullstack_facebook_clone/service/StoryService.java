package com.springboot.fullstack_facebook_clone.service;

import org.springframework.web.multipart.MultipartFile;

public interface StoryService {

    void createStory(Long userId, String text, MultipartFile file);
}
