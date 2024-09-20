package com.springboot.fullstack_facebook_clone.controller;

import com.springboot.fullstack_facebook_clone.service.PostImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostImageController {

    private final PostImageService postImageService;

    @PostMapping("/post/image/upload")
    public void uploadPostImages(@RequestParam(value = "postId")Long postId, @RequestParam(value = "file") MultipartFile[] files){
        postImageService.uploadPostImages(postId,files);
    }

    @GetMapping(path = "/post/image/{filename}", produces = {IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE})
    public byte[] getImages(@PathVariable("filename") String filename) throws IOException {
        return postImageService.getImages(filename);
    }
}
