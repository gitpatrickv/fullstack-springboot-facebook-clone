package com.springboot.fullstack_facebook_clone.controller;

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
}
