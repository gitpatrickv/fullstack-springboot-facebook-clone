package com.springboot.fullstack_facebook_clone.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageModel {
    private Long productImageId;
    private String productImage;
}
