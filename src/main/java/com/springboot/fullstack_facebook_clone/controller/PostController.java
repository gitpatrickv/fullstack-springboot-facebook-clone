package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.dto.response.ErrorResponse;
import com.springboot.fullstack_facebook_clone.dto.response.PostListResponse;
import com.springboot.fullstack_facebook_clone.service.PostService;
import com.springboot.fullstack_facebook_clone.service.UserService;
import com.springboot.fullstack_facebook_clone.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @PostMapping(value = {"/save"},  consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createPost(@RequestPart(value="post", required = false) String content,
                                        @RequestPart(value = "file", required = false) MultipartFile[] files)
    {
        String currentUser = userService.getAuthenticatedUser();
        try {
            postService.createPost(currentUser, content, files);
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            ErrorResponse errorResponse = new ErrorResponse(StringUtil.USER_NOT_FOUND + currentUser);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(StringUtil.ERROR_MESSAGE);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping
    public ResponseEntity<?> fetchAllUserPosts(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                               @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        String currentUser = userService.getAuthenticatedUser();
        try {
            PostListResponse userPosts = postService.fetchAllUserPosts(currentUser, pageNo, pageSize);
            return new ResponseEntity<>(userPosts, HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            ErrorResponse errorResponse = new ErrorResponse(StringUtil.USER_NOT_FOUND + currentUser);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(StringUtil.ERROR_MESSAGE);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}



