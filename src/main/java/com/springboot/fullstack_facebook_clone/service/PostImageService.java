package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.response.PhotoListResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PostImageService {

    void uploadPostImages(Long postId, MultipartFile[] files);
    byte[] getImages(String filename) throws IOException;
    String processPostImages(Long postId, MultipartFile image);
    PhotoListResponse fetchAllPhotos(Long userId, int pageNo, int pageSize);
}
