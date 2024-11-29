package com.springboot.fullstack_facebook_clone.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springboot.fullstack_facebook_clone.entity.constants.Availability;
import com.springboot.fullstack_facebook_clone.entity.constants.Category;
import com.springboot.fullstack_facebook_clone.entity.constants.ProductCondition;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_DEFAULT)
public class ProductModel {

    private Long productId;
    @NotNull
    private Category category;
    @NotNull
    private ProductCondition productCondition;
    @NotNull
    private Availability availability;
    @NotNull
    private String productName;
    @NotNull
    private Double price;
    private String brand;
    private String description;
    private List<ProductImageModel> productImages = new ArrayList<>();
    private UserDataModel user;

}
