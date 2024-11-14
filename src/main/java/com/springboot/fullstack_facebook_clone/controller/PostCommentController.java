package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.dto.model.PostCommentModel;
import com.springboot.fullstack_facebook_clone.dto.response.PostCommentCountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostCommentListResponse;
import com.springboot.fullstack_facebook_clone.service.PostCommentService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostCommentController {

    private final UserService userService;
    private final PostCommentService postCommentService;
    @PostMapping("/{postId}/comment")
    public void writePostComment(@PathVariable("postId") Long postId,
                                 @RequestPart(value="comment", required = false)String comment,
                                 @RequestPart(value = "file", required = false) MultipartFile file){
        String currentUser = userService.getAuthenticatedUser();
        postCommentService.writePostComment(currentUser,postId, comment, file);
    }

    @GetMapping("/{postId}/comment")
    public PostCommentListResponse fetchAllPostComments(@PathVariable("postId") Long postId,
                                                        @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        return postCommentService.fetchAllPostComments(postId,pageNo,pageSize);
    }
    @GetMapping("/{postId}/comment/count")
    public PostCommentCountResponse getCommentCount(@PathVariable("postId") Long postId){
        return postCommentService.getCommentCount(postId);
    }

    @GetMapping("/{postId}/comment/last")
    public PostCommentModel getLastComment(@PathVariable("postId") Long postId) {
        return postCommentService.getLastComment(postId);
    }
}
