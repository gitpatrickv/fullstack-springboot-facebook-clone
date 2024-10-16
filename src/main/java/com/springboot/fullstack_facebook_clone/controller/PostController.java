package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.dto.request.SharePostRequest;
import com.springboot.fullstack_facebook_clone.dto.response.PostListResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostResponse;
import com.springboot.fullstack_facebook_clone.dto.response.SharedPostCountResponse;
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

    @PostMapping(value = {"/save/{userId}"},  consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public void createPost(@PathVariable("userId") Long userId,
                        @RequestPart(value="post", required = false) String content,
                        @RequestPart(value = "file", required = false) MultipartFile[] files)
    {
        String currentUser = userService.getAuthenticatedUser();
        postService.createPost(currentUser,userId, content, files);
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

    @GetMapping("/share/count/{postId}")
    public SharedPostCountResponse getSharedPostCount(@PathVariable("postId") Long postId) {
        return postService.getSharedPostCount(postId);
    }
    @PostMapping("/share/image/{postId}/{postImageId}")
    public void sharePostImage(@PathVariable("postId") Long postId,
                               @PathVariable("postImageId") Long postImageId,
                               @RequestBody(required = false) SharePostRequest request) {
        String currentUser = userService.getAuthenticatedUser();
        postService.sharePostImage(currentUser, postImageId, postId,request);
    }
    @GetMapping("/share/image/count/{postImageId}")
    public SharedPostCountResponse getSharedPostImageCount(@PathVariable("postImageId")Long postImageId) {
        return postService.getSharedPostImageCount(postImageId);
    }
    @DeleteMapping("/delete/{postId}")
    public void deletePost(@PathVariable("postId") Long postId){
        String currentUser = userService.getAuthenticatedUser();
        postService.deletePost(currentUser, postId);
    }

    @GetMapping("/find/{postId}")
    public PostResponse findPostById(@PathVariable("postId") Long postId) {
        return postService.findPostById(postId);
    }
    @GetMapping("/get/all")
    public PostListResponse fetchAllPosts(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        String currentUser = userService.getAuthenticatedUser();
        return postService.fetchAllPosts(currentUser,pageNo,pageSize);
    }
}



