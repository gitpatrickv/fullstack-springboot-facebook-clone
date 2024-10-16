package com.springboot.fullstack_facebook_clone.repository;

import com.springboot.fullstack_facebook_clone.entity.PostImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    @Query("SELECT pi FROM PostImage pi JOIN pi.post p WHERE p.user.id = :userId")
    Page<PostImage> findAllPostImagesByUserId(@Param("userId") Long userId, Pageable pageable);

}
