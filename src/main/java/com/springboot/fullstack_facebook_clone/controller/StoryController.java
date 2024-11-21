package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/story")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;
    @PostMapping("/create/{userId}")
    public void createStory(@PathVariable("userId") Long userId,
                            @RequestPart(value = "text", required = false) String text,
                            @RequestPart(value = "file", required = false) MultipartFile file){
        storyService.createStory(userId,text,file);
    }
}
