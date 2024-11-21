package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.entity.Story;
import com.springboot.fullstack_facebook_clone.entity.User;
import com.springboot.fullstack_facebook_clone.repository.StoryRepository;
import com.springboot.fullstack_facebook_clone.repository.UserRepository;
import com.springboot.fullstack_facebook_clone.service.StoryService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public void createStory(Long userId, String text, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + userId));

        Story story = new Story();
        story.setText(text);
        if(file != null) {
            story.setStoryImage(userService.processImage(file));
        }
        story.setTimestamp(LocalDateTime.now());
        story.setUser(user);
        storyRepository.save(story);
    }
}
