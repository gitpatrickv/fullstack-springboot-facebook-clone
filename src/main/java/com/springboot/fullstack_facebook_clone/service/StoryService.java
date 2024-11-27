package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.response.StoryListResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoryService {

    void createStory(Long userId, String text, MultipartFile file);
    List<StoryListResponse> fetchAllStories(Long userId);
    void deleteStory(Long storyId);
}
