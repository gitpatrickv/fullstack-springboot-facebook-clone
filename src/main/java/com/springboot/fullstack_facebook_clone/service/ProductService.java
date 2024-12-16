package com.springboot.fullstack_facebook_clone.service;

import com.springboot.fullstack_facebook_clone.dto.model.ProductModel;
import com.springboot.fullstack_facebook_clone.dto.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    void saveProduct(ProductModel productModel,  MultipartFile[] files);
    ProductResponse fetchAllProducts(int pageNo, int pageSize);
    ProductResponse fetchAllProductsByCategory(String category, int pageNo, int pageSize);
    ProductResponse fetchAllUserListedProducts(Long userId, int pageNo, int pageSize);
    ProductResponse searchItem(String search, int pageNo, int pageSize);
    ProductModel findProductById(Long productId);
}
