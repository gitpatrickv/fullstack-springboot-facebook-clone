package com.springboot.fullstack_facebook_clone.service;

import org.springframework.web.multipart.MultipartFile;

public interface ProductImageService {
    void uploadImages(Long productId, MultipartFile[] files);
}
