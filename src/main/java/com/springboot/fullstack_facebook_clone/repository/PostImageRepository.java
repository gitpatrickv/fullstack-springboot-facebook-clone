package com.springboot.fullstack_facebook_clone.repository;

import com.springboot.fullstack_facebook_clone.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
