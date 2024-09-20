package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.service.PostService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @PostMapping(value = {"/post/save"},  consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void createPost(@RequestPart(value="content", required = false) String content,
                           @RequestPart(value = "file", required = false) MultipartFile[] files)
        {
            String currentUser = userService.getAuthenticatedUser();
            postService.createPost(currentUser, content, files);
        }
    }

