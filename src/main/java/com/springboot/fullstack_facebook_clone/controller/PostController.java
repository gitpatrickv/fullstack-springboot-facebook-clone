package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.dto.request.SharePostRequest;
import com.springboot.fullstack_facebook_clone.dto.response.PostListResponse;
import com.springboot.fullstack_facebook_clone.service.PostService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @PostMapping(value = {"/save"},  consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public void createPost(@RequestPart(value="post", required = false) String content,
                                        @RequestPart(value = "file", required = false) MultipartFile[] files)
    {
        String currentUser = userService.getAuthenticatedUser();
        postService.createPost(currentUser, content, files);
    }
    @GetMapping("/{userId}")
    public PostListResponse fetchAllUserPosts(@PathVariable Long userId,
                                                @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                               @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return postService.fetchAllUserPosts(userId,pageNo,pageSize);
    }
    @PostMapping("/share/{postId}")
    public void sharePost(@PathVariable("postId") Long postId, @RequestBody(required = false) SharePostRequest request) {
        String currentUser = userService.getAuthenticatedUser();
        postService.sharePost(currentUser,postId,request);
    }
}



