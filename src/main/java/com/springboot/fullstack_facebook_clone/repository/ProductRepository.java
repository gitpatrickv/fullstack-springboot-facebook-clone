package com.springboot.fullstack_facebook_clone.repository;

import com.springboot.fullstack_facebook_clone.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE LOWER(p.category) = LOWER(:category)")
    Page<Product> findProductsByCategory(@Param("category") String category, Pageable pageable);

    Page<Product> findAllByUser_UserId(Long userId, Pageable pageable);
    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER (p.brand) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Product> searchItem(@Param("search") String search, Pageable pageable);

}
