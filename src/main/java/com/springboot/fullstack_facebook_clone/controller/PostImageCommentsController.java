package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.dto.response.PostCommentCountResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostCommentListResponse;
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
    @GetMapping("/{postImageId}/image/comment")
    public PostCommentListResponse fetchAllPostImageComments(@PathVariable("postImageId") Long postImageId,
                                                             @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                             @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return postImageCommentsService.fetchAllPostImageComments(postImageId,pageNo,pageSize);
    }
    @GetMapping("/{postImageId}/image/comment/count")
    public PostCommentCountResponse getPostImageCommentCount(@PathVariable("postImageId") Long postImageId) {
        return postImageCommentsService.getPostImageCommentCount(postImageId);
    }
}
