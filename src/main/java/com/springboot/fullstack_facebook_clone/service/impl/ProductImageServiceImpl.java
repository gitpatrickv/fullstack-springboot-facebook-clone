package com.springboot.fullstack_facebook_clone.service.impl;

import com.springboot.fullstack_facebook_clone.entity.Product;
import com.springboot.fullstack_facebook_clone.entity.ProductImage;
import com.springboot.fullstack_facebook_clone.repository.ProductImageRepository;
import com.springboot.fullstack_facebook_clone.repository.ProductRepository;
import com.springboot.fullstack_facebook_clone.service.PostImageService;
import com.springboot.fullstack_facebook_clone.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final PostImageService postImageService;
    @Override
    public void uploadImages(Long productId, MultipartFile[] files) {
        Optional<Product> product = productRepository.findById(productId);

        if(product.isPresent()) {
            for (MultipartFile file : files) {
                ProductImage productImage = new ProductImage();
                productImage.setProductImage(postImageService.processPostImages(productId, file));
                productImage.setProduct(product.get());
                productImageRepository.save(productImage);
            }
        }
    }
}
