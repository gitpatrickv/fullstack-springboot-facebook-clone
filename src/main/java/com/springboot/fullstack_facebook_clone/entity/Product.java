package com.springboot.fullstack_facebook_clone.entity;

import com.springboot.fullstack_facebook_clone.entity.constants.Availability;
import com.springboot.fullstack_facebook_clone.entity.constants.Category;
import com.springboot.fullstack_facebook_clone.entity.constants.ProductCondition;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product extends Timestamp{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Enumerated(EnumType.STRING)
    private ProductCondition productCondition;
    @Enumerated(EnumType.STRING)
    private Availability availability;
    private String productName;
    private Double price;
    private String brand;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "product",  cascade = CascadeType.ALL)
    private List<ProductImage> productImages = new ArrayList<>();
}
