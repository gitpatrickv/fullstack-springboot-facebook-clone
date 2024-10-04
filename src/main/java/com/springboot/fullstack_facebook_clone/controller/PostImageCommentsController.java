package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.service.PostImageCommentsService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostImageCommentsController {

    private final UserService userService;
    private final PostImageCommentsService postImageCommentsService;
    @PostMapping("/{postImageId}/image/comment")
    public void writePostImageComment(@PathVariable("postImageId")Long postImageId,
                                      @RequestPart(value="comment", required = false)String comment,
                                      @RequestPart(value = "file", required = false) MultipartFile file){
        String currentUser = userService.getAuthenticatedUser();
        postImageCommentsService.writePostImageComment(currentUser, postImageId, comment, file);
    }
}
